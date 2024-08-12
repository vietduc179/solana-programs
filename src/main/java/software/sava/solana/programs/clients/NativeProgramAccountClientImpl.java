package software.sava.solana.programs.clients;

import software.sava.core.accounts.AccountWithSeed;
import software.sava.core.accounts.ProgramDerivedAddress;
import software.sava.core.accounts.PublicKey;
import software.sava.core.accounts.SolanaAccounts;
import software.sava.core.accounts.lookup.AddressLookupTable;
import software.sava.core.accounts.meta.AccountMeta;
import software.sava.core.accounts.meta.LookupTableAccountMeta;
import software.sava.core.accounts.token.TokenAccount;
import software.sava.core.tx.Instruction;
import software.sava.core.tx.Transaction;
import software.sava.rpc.json.http.client.SolanaRpcClient;
import software.sava.rpc.json.http.response.AccountInfo;
import software.sava.solana.programs.address_lookup_table.AddressLookupTableProgram;
import software.sava.solana.programs.stake.StakeAccount;
import software.sava.solana.programs.stake.StakeProgram;
import software.sava.solana.programs.stake.StakeState;
import software.sava.solana.programs.system.SystemProgram;
import software.sava.solana.programs.token.AssociatedTokenProgram;
import software.sava.solana.programs.token.TokenProgram;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static software.sava.solana.programs.compute_budget.ComputeBudgetProgram.COMPUTE_UNITS_CONSUMED;

final class NativeProgramAccountClientImpl implements NativeProgramAccountClient {

  private final SolanaAccounts accounts;
  private final NativeProgramClient nativeProgramClient;
  private final AccountMeta owner;
  private final ProgramDerivedAddress wrappedSolPDA;

  NativeProgramAccountClientImpl(final SolanaAccounts accounts, final AccountMeta owner) {
    this.accounts = accounts;
    this.nativeProgramClient = NativeProgramClient.createClient(accounts);
    this.owner = owner;
    this.wrappedSolPDA = findAssociatedTokenProgramAddress(accounts.wrappedSolTokenMint());
  }

  NativeProgramAccountClientImpl(final NativeProgramClient nativeProgramClient, final AccountMeta owner) {
    this.accounts = nativeProgramClient.accounts();
    this.nativeProgramClient = nativeProgramClient;
    this.owner = owner;
    this.wrappedSolPDA = findAssociatedTokenProgramAddress(accounts.wrappedSolTokenMint());
  }

  @Override
  public AccountMeta owner() {
    return owner;
  }

  @Override
  public SolanaAccounts accounts() {
    return accounts;
  }

  @Override
  public ProgramDerivedAddress wrappedSolPDA() {
    return wrappedSolPDA;
  }

  @Override
  public NativeProgramClient nativeProgramClient() {
    return nativeProgramClient;
  }

  @Override
  public Transaction createTransaction(final PublicKey feePayer, final Instruction instruction) {
    return Transaction.createTx(feePayer, instruction);
  }

  @Override
  public Transaction createTransaction(final PublicKey feePayer, final List<Instruction> instructions) {
    return Transaction.createTx(feePayer, instructions);
  }

  @Override
  public Transaction createTransaction(final AccountMeta feePayer, final Instruction instruction) {
    return Transaction.createTx(feePayer, instruction);
  }

  @Override
  public Transaction createTransaction(final AccountMeta feePayer, final List<Instruction> instructions) {
    return Transaction.createTx(feePayer, instructions);
  }

  @Override
  public Transaction createTransaction(final PublicKey feePayer,
                                       final int computeUnitLimit,
                                       final long microLamportComputeUnitPrice,
                                       final Instruction instruction) {
    return Transaction.createTx(feePayer, List.of(
        nativeProgramClient.computeUnitLimit(computeUnitLimit + COMPUTE_UNITS_CONSUMED),
        nativeProgramClient.computeUnitPrice(microLamportComputeUnitPrice),
        instruction
    ));
  }

  @Override
  public Transaction createTransaction(final AccountMeta feePayer,
                                       final int computeUnitLimit,
                                       final long microLamportComputeUnitPrice,
                                       final Instruction instruction) {
    return Transaction.createTx(feePayer, List.of(
        nativeProgramClient.computeUnitLimit(computeUnitLimit + COMPUTE_UNITS_CONSUMED),
        nativeProgramClient.computeUnitPrice(microLamportComputeUnitPrice),
        instruction
    ));
  }

  @Override
  public Transaction createTransaction(final Instruction instruction) {
    return createTransaction(owner, instruction);
  }

  @Override
  public Transaction createTransaction(final List<Instruction> instructions) {
    return createTransaction(owner, instructions);
  }

  @Override
  public Transaction createTransaction(final int computeUnitLimit,
                                       final long microLamportComputeUnitPrice,
                                       final Instruction instruction) {
    return createTransaction(owner, computeUnitLimit, microLamportComputeUnitPrice, instruction);
  }

  @Override
  public Transaction createTransaction(final int computeUnitLimit,
                                       final long microLamportComputeUnitPrice,
                                       final List<Instruction> instructions) {
    return createTransaction(computeUnitLimit, microLamportComputeUnitPrice, Transaction.createTx(owner, instructions));
  }

  @Override
  public Transaction createTransaction(final int computeUnitLimit,
                                       final long microLamportComputeUnitPrice,
                                       final Transaction transaction) {
    return transaction.prependInstructions(
        nativeProgramClient.computeUnitLimit(computeUnitLimit + COMPUTE_UNITS_CONSUMED),
        nativeProgramClient.computeUnitPrice(microLamportComputeUnitPrice)
    );
  }

  @Override
  public Transaction createTransaction(final PublicKey feePayer,
                                       final Instruction instruction,
                                       final AddressLookupTable lookupTable) {
    return Transaction.createTx(feePayer, List.of(instruction), lookupTable);
  }

  @Override
  public Transaction createTransaction(final PublicKey feePayer,
                                       final List<Instruction> instructions,
                                       final AddressLookupTable lookupTable) {
    return Transaction.createTx(feePayer, instructions, lookupTable);
  }

  @Override
  public Transaction createTransaction(final AccountMeta feePayer,
                                       final Instruction instruction,
                                       final AddressLookupTable lookupTable) {
    return Transaction.createTx(feePayer, List.of(instruction), lookupTable);
  }

  @Override
  public Transaction createTransaction(final AccountMeta feePayer,
                                       final List<Instruction> instructions,
                                       final AddressLookupTable lookupTable) {
    return Transaction.createTx(feePayer, instructions, lookupTable);
  }

  @Override
  public Transaction createTransaction(final PublicKey feePayer,
                                       final int computeUnitLimit,
                                       final long microLamportComputeUnitPrice,
                                       final Instruction instruction,
                                       final AddressLookupTable lookupTable) {
    return Transaction.createTx(feePayer, List.of(
            nativeProgramClient.computeUnitLimit(computeUnitLimit + COMPUTE_UNITS_CONSUMED),
            nativeProgramClient.computeUnitPrice(microLamportComputeUnitPrice),
            instruction
        ),
        lookupTable
    );
  }

  @Override
  public Transaction createTransaction(final AccountMeta feePayer,
                                       final int computeUnitLimit,
                                       final long microLamportComputeUnitPrice,
                                       final Instruction instruction,
                                       final AddressLookupTable lookupTable) {
    return Transaction.createTx(feePayer, List.of(
            nativeProgramClient.computeUnitLimit(computeUnitLimit + COMPUTE_UNITS_CONSUMED),
            nativeProgramClient.computeUnitPrice(microLamportComputeUnitPrice),
            instruction
        ),
        lookupTable
    );
  }

  @Override
  public Transaction createTransaction(final Instruction instruction, final AddressLookupTable lookupTable) {
    return createTransaction(owner, instruction, lookupTable);
  }

  @Override
  public Transaction createTransaction(final List<Instruction> instructions, final AddressLookupTable lookupTable) {
    return createTransaction(owner, instructions, lookupTable);
  }

  @Override
  public Transaction createTransaction(final int computeUnitLimit,
                                       final long microLamportComputeUnitPrice,
                                       final Instruction instruction,
                                       final AddressLookupTable lookupTable) {
    return createTransaction(owner, computeUnitLimit, microLamportComputeUnitPrice, instruction, lookupTable);
  }


  @Override
  public Transaction createTransaction(final int computeUnitLimit,
                                       final long microLamportComputeUnitPrice,
                                       final List<Instruction> instructions,
                                       final AddressLookupTable lookupTable) {
    return createTransaction(computeUnitLimit, microLamportComputeUnitPrice, Transaction.createTx(owner, instructions, lookupTable));
  }

  @Override
  public Transaction createTransaction(final PublicKey feePayer,
                                       final Instruction instruction,
                                       final LookupTableAccountMeta[] tableAccountMetas) {
    return Transaction.createTx(feePayer, List.of(instruction), tableAccountMetas);
  }

  @Override
  public Transaction createTransaction(final PublicKey feePayer,
                                       final List<Instruction> instructions,
                                       final LookupTableAccountMeta[] tableAccountMetas) {
    return Transaction.createTx(feePayer, instructions, tableAccountMetas);
  }

  @Override
  public Transaction createTransaction(final AccountMeta feePayer,
                                       final Instruction instruction,
                                       final LookupTableAccountMeta[] tableAccountMetas) {
    return Transaction.createTx(feePayer, List.of(instruction), tableAccountMetas);
  }

  @Override
  public Transaction createTransaction(final AccountMeta feePayer,
                                       final List<Instruction> instructions,
                                       final LookupTableAccountMeta[] tableAccountMetas) {
    return Transaction.createTx(feePayer, instructions, tableAccountMetas);
  }

  @Override
  public Transaction createTransaction(final PublicKey feePayer,
                                       final int computeUnitLimit,
                                       final long microLamportComputeUnitPrice,
                                       final Instruction instruction,
                                       final LookupTableAccountMeta[] tableAccountMetas) {
    return Transaction.createTx(feePayer, List.of(
            nativeProgramClient.computeUnitLimit(computeUnitLimit + COMPUTE_UNITS_CONSUMED),
            nativeProgramClient.computeUnitPrice(microLamportComputeUnitPrice),
            instruction
        ),
        tableAccountMetas
    );
  }

  @Override
  public Transaction createTransaction(final AccountMeta feePayer,
                                       final int computeUnitLimit,
                                       final long microLamportComputeUnitPrice,
                                       final Instruction instruction,
                                       final LookupTableAccountMeta[] tableAccountMetas) {
    return Transaction.createTx(feePayer, List.of(
            nativeProgramClient.computeUnitLimit(computeUnitLimit + COMPUTE_UNITS_CONSUMED),
            nativeProgramClient.computeUnitPrice(microLamportComputeUnitPrice),
            instruction
        ),
        tableAccountMetas
    );
  }

  @Override
  public Transaction createTransaction(final Instruction instruction, final LookupTableAccountMeta[] tableAccountMetas) {
    return createTransaction(owner, instruction, tableAccountMetas);
  }

  @Override
  public Transaction createTransaction(final List<Instruction> instructions, final LookupTableAccountMeta[] tableAccountMetas) {
    return createTransaction(owner, instructions, tableAccountMetas);
  }

  @Override
  public Transaction createTransaction(final int computeUnitLimit,
                                       final long microLamportComputeUnitPrice,
                                       final Instruction instruction,
                                       final LookupTableAccountMeta[] tableAccountMetas) {
    return createTransaction(owner, computeUnitLimit, microLamportComputeUnitPrice, instruction, tableAccountMetas);
  }

  @Override
  public Transaction createTransaction(final int computeUnitLimit,
                                       final long microLamportComputeUnitPrice,
                                       final List<Instruction> instructions,
                                       final LookupTableAccountMeta[] tableAccountMetas) {
    return createTransaction(computeUnitLimit, microLamportComputeUnitPrice, Transaction.createTx(owner, instructions, tableAccountMetas));
  }

  @Override
  public ProgramDerivedAddress findAssociatedTokenProgramAddress(final PublicKey mint) {
    return AssociatedTokenProgram.findAssociatedTokenProgramAddress(accounts, owner.publicKey(), mint);
  }

  @Override
  public CompletableFuture<List<AccountInfo<TokenAccount>>> fetchTokenAccounts(final SolanaRpcClient rpcClient, final PublicKey tokenMintAddress) {
    return rpcClient.getTokenAccountsForTokenMintByOwner(owner.publicKey(), tokenMintAddress);
  }

  @Override
  public CompletableFuture<List<AccountInfo<TokenAccount>>> fetchTokenAccounts(final SolanaRpcClient rpcClient) {
    return rpcClient.getTokenAccountsForProgramByOwner(owner.publicKey(), accounts.tokenProgram());
  }

  @Override
  public CompletableFuture<List<AccountInfo<TokenAccount>>> fetchToken2022Accounts(final SolanaRpcClient rpcClient) {
    return rpcClient.getTokenAccountsForProgramByOwner(owner.publicKey(), accounts.token2022Program());
  }

  @Override
  public Instruction createAccount(final PublicKey newAccountPublicKey,
                                   final long lamports,
                                   final long space,
                                   final PublicKey programOwner) {
    return SystemProgram.createAccount(
        accounts.invokedSystemProgram(),
        owner,
        newAccountPublicKey,
        lamports,
        space,
        programOwner
    );
  }

  @Override
  public Instruction createAccountWithSeed(final AccountWithSeed accountWithSeed,
                                           final long lamports,
                                           final long space,
                                           final PublicKey programOwner) {
    return SystemProgram.createAccountWithSeed(
        accounts.invokedSystemProgram(),
        owner,
        accountWithSeed,
        lamports,
        space,
        programOwner
    );
  }

  @Override
  public Instruction allocateAccountSpaceWithSeed(final AccountWithSeed accountWithSeed,
                                                  final long space,
                                                  final PublicKey programOwner) {
    return SystemProgram.allocateWithSeed(
        accounts.invokedSystemProgram(),
        accountWithSeed,
        owner,
        space,
        programOwner
    );
  }

  @Override
  public AccountWithSeed createOffCurveAccountWithSeed(final String asciiSeed) {
    return PublicKey.createOffCurveAccountWithAsciiSeed(owner.publicKey(), asciiSeed, accounts.stakeProgram());
  }

  @Override
  public CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsByStakeAuthority(final SolanaRpcClient rpcClient,
                                                                                               final StakeState stakeState) {
    return nativeProgramClient.fetchStakeAccountsByStakeAuthority(rpcClient, stakeState, this.owner.publicKey());
  }

  @Override
  public CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsByWithdrawAuthority(final SolanaRpcClient rpcClient,
                                                                                                  final StakeState stakeState) {
    return nativeProgramClient.fetchStakeAccountsByWithdrawAuthority(rpcClient, stakeState, this.owner.publicKey());
  }

  @Override
  public CompletableFuture<List<AccountInfo<AddressLookupTable>>> fetchLookupTableAccountsByAuthority(final SolanaRpcClient rpcClient) {
    return nativeProgramClient.fetchLookupTableAccountsByAuthority(rpcClient, ownerPublicKey());
  }

  @Override
  public Instruction createStakeAccount(final PublicKey newAccountPublicKey, final long lamports) {
    return createAccount(
        newAccountPublicKey,
        lamports,
        StakeAccount.BYTES,
        accounts.stakeProgram()
    );
  }

  @Override
  public Instruction createStakeAccountWithSeed(final AccountWithSeed accountWithSeed, final long lamports) {
    return createAccountWithSeed(
        accountWithSeed,
        lamports,
        StakeAccount.BYTES,
        accounts.stakeProgram()
    );
  }

  @Override
  public Instruction allocateStakeAccountWithSeed(final AccountWithSeed accountWithSeed) {
    return allocateAccountSpaceWithSeed(
        accountWithSeed,
        StakeAccount.BYTES,
        accounts.stakeProgram());
  }

  @Override
  public Instruction transferSolLamportsWithSeed(final AccountWithSeed accountWithSeed,
                                                 final long lamports,
                                                 final PublicKey programOwner) {
    return SystemProgram.transferWithSeed(
        accounts.invokedSystemProgram(),
        accountWithSeed,
        this.owner,
        this.owner.publicKey(),
        lamports,
        programOwner
    );
  }

  @Override
  public Instruction transferSolLamports(final PublicKey toPublicKey, final long lamports) {
    return SystemProgram.transfer(accounts.invokedSystemProgram(), owner, toPublicKey, lamports);
  }

  @Override
  public Instruction transferToken(final PublicKey fromTokenAccount,
                                   final PublicKey toTokenAccount,
                                   final long lamports) {
    return TokenProgram.transfer(
        accounts.invokedTokenProgram(),
        fromTokenAccount,
        toTokenAccount,
        lamports,
        owner
    );
  }

  @Override
  public Instruction transferTokenChecked(final PublicKey fromTokenAccount,
                                          final PublicKey toTokenAccount,
                                          final long lamports,
                                          final int decimals,
                                          final PublicKey tokenMint) {
    return TokenProgram.transferChecked(
        accounts.invokedTokenProgram(),
        fromTokenAccount,
        toTokenAccount,
        lamports,
        decimals,
        owner,
        tokenMint
    );
  }

  @Override
  public Instruction closeTokenAccount(final PublicKey tokenAccount) {
    return TokenProgram.closeAccount(
        accounts.invokedTokenProgram(),
        tokenAccount,
        owner.publicKey(),
        owner
    );
  }

  @Override
  public Instruction createATAFor(final boolean idempotent,
                                  final PublicKey tokenAccountOwner,
                                  final PublicKey programDerivedAddress,
                                  final PublicKey mint) {
    return AssociatedTokenProgram.createATA(
        idempotent,
        accounts,
        owner,
        programDerivedAddress,
        tokenAccountOwner,
        mint
    );
  }

  @Override
  public Instruction createATAFor(final boolean idempotent, final PublicKey tokenAccountOwner, final PublicKey mint) {
    return AssociatedTokenProgram.createATA(
        idempotent,
        accounts,
        owner,
        tokenAccountOwner,
        mint
    );
  }

  @Override
  public Instruction createATA(final boolean idempotent, final PublicKey programDerivedAddress, final PublicKey mint) {
    return createATAFor(idempotent, owner.publicKey(), programDerivedAddress, mint);
  }

  @Override
  public Instruction createATA(final boolean idempotent, final PublicKey mint) {
    return createATAFor(idempotent, owner.publicKey(), mint);
  }

  @Override
  public Instruction initializeStakeAccountWithStaker(final PublicKey unInitializedStakeAccount,
                                                      final PublicKey staker) {
    return nativeProgramClient.initializeStakeAccountChecked(
        unInitializedStakeAccount,
        staker,
        owner
    );
  }

  @Override
  public Instruction initializeStakeAccountWithWithdrawer(final PublicKey unInitializedStakeAccount,
                                                          final PublicKey withdrawer) {
    return nativeProgramClient.initializeStakeAccount(
        unInitializedStakeAccount,
        owner.publicKey(),
        withdrawer
    );
  }

  @Override
  public Instruction initializeStakeAccount(final PublicKey unInitializedStakeAccount) {
    return nativeProgramClient.initializeStakeAccount(
        unInitializedStakeAccount,
        owner.publicKey(),
        owner.publicKey()
    );
  }

  @Override
  public Instruction initializeStakeAccountChecked(final PublicKey unInitializedStakeAccount,
                                                   final PublicKey staker) {
    return nativeProgramClient.initializeStakeAccountChecked(
        unInitializedStakeAccount,
        staker,
        owner
    );
  }

  @Override
  public Instruction initializeStakeAccountChecked(final PublicKey unInitializedStakeAccount,
                                                   final AccountMeta withdrawer) {
    return nativeProgramClient.initializeStakeAccountChecked(
        unInitializedStakeAccount,
        owner.publicKey(),
        withdrawer
    );
  }

  @Override
  public Instruction initializeStakeAccountChecked(final PublicKey unInitializedStakeAccount) {
    return nativeProgramClient.initializeStakeAccountChecked(
        unInitializedStakeAccount,
        owner.publicKey(),
        owner
    );
  }

  @Override
  public Instruction authorizeStakeAccount(final PublicKey stakeAccount,
                                           final PublicKey newAuthority,
                                           final StakeProgram.StakeAuthorize stakeAuthorize) {
    return nativeProgramClient.authorizeStakeAccount(
        stakeAccount,
        owner,
        newAuthority,
        stakeAuthorize
    );
  }

  @Override
  public Instruction delegateStakeAccount(final PublicKey initializedStakeAccount,
                                          final PublicKey validatorVoteAccount) {
    return StakeProgram.delegateStake(
        accounts,
        initializedStakeAccount,
        validatorVoteAccount,
        owner
    );
  }

  @Override
  public Instruction reDelegateStakeAccount(final PublicKey delegatedStakeAccount,
                                            final PublicKey uninitializedStakeAccount,
                                            final PublicKey validatorVoteAccount) {
    return StakeProgram.reDelegate(
        accounts,
        delegatedStakeAccount,
        uninitializedStakeAccount,
        validatorVoteAccount,
        owner
    );
  }

  @Override
  public Instruction deactivateStakeAccount(final PublicKey delegatedStakeAccount) {
    return StakeProgram.deactivate(accounts, delegatedStakeAccount, owner);
  }

  @Override
  public List<Instruction> deactivateStakeAccountInfos(final Collection<AccountInfo<StakeAccount>> delegatedStakeAccounts) {
    return delegatedStakeAccounts.stream().map(this::deactivateStakeAccount).toList();
  }

  @Override
  public List<Instruction> deactivateStakeAccounts(final Collection<StakeAccount> delegatedStakeAccounts) {
    return delegatedStakeAccounts.stream().map(this::deactivateStakeAccount).toList();
  }

  @Override
  public List<Instruction> deactivateStakeAccountKeys(final Collection<PublicKey> delegatedStakeAccounts) {
    return delegatedStakeAccounts.stream().map(this::deactivateStakeAccount).toList();
  }

  @Override
  public Instruction splitStakeAccount(final PublicKey splitStakeAccount,
                                       final PublicKey unInitializedStakeAccount,
                                       final long lamports) {
    return nativeProgramClient.splitStakeAccount(
        splitStakeAccount,
        unInitializedStakeAccount,
        owner,
        lamports
    );
  }

  @Override
  public Instruction mergeStakeAccounts(final PublicKey destinationStakeAccount,
                                        final PublicKey srcStakeAccount) {
    return nativeProgramClient.mergeStakeAccounts(
        destinationStakeAccount,
        srcStakeAccount,
        owner
    );
  }

  @Override
  public List<Instruction> mergeStakeAccountInto(final PublicKey destinationStakeAccount, final Collection<PublicKey> stakeAccounts) {
    return stakeAccounts.stream()
        .map(stakeAccount -> mergeStakeAccounts(destinationStakeAccount, stakeAccount))
        .toList();
  }

  @Override
  public List<Instruction> mergeStakeAccountsInto(final StakeAccount destinationStakeAccount, final Collection<StakeAccount> stakeAccounts) {
    return stakeAccounts.stream()
        .map(stakeAccount -> mergeStakeAccounts(destinationStakeAccount, stakeAccount))
        .toList();
  }

  @Override
  public List<Instruction> mergeStakeAccountsInto(final AccountInfo<StakeAccount> destinationStakeAccount, final Collection<AccountInfo<StakeAccount>> stakeAccounts) {
    return stakeAccounts.stream()
        .map(stakeAccount -> mergeStakeAccounts(destinationStakeAccount, stakeAccount))
        .toList();
  }

  @Override
  public List<Instruction> mergeStakeAccountKeys(final List<PublicKey> stakeAccounts) {
    if (stakeAccounts.size() < 2) {
      return List.of();
    } else {
      final var mergeInto = stakeAccounts.getFirst();
      return stakeAccounts.stream().skip(1)
          .map(stakeAccount -> mergeStakeAccounts(mergeInto, stakeAccount))
          .toList();
    }
  }

  @Override
  public List<Instruction> mergeStakeAccounts(final List<StakeAccount> stakeAccounts) {
    if (stakeAccounts.size() < 2) {
      return List.of();
    } else {
      final var mergeInto = stakeAccounts.getFirst();
      return stakeAccounts.stream().skip(1)
          .map(stakeAccount -> mergeStakeAccounts(mergeInto, stakeAccount))
          .toList();
    }
  }

  @Override
  public List<Instruction> mergeStakeAccountInfos(final List<AccountInfo<StakeAccount>> stakeAccounts) {
    if (stakeAccounts.size() < 2) {
      return List.of();
    } else {
      final var mergeInto = stakeAccounts.getFirst();
      return stakeAccounts.stream().skip(1)
          .map(stakeAccount -> mergeStakeAccounts(mergeInto, stakeAccount))
          .toList();
    }
  }

  @Override
  public List<Instruction> mergeStakeAccountKeys(final Collection<PublicKey> stakeAccounts) {
    if (stakeAccounts.size() < 2) {
      return List.of();
    } else {
      final var array = stakeAccounts.toArray(PublicKey[]::new);
      final var mergeInto = array[0];
      return Arrays.stream(array, 1, array.length)
          .map(accountInfo -> mergeStakeAccounts(mergeInto, accountInfo))
          .toList();
    }
  }

  @Override
  public List<Instruction> mergeStakeAccounts(final Collection<StakeAccount> stakeAccounts) {
    if (stakeAccounts.size() < 2) {
      return List.of();
    } else {
      final var array = stakeAccounts.toArray(StakeAccount[]::new);
      final var mergeInto = array[0];
      return Arrays.stream(array, 1, array.length)
          .map(accountInfo -> mergeStakeAccounts(mergeInto, accountInfo))
          .toList();
    }
  }

  @Override
  public List<Instruction> mergeStakeAccountInfos(final Collection<AccountInfo<StakeAccount>> stakeAccounts) {
    final var array = stakeAccounts.toArray(AccountInfo[]::new);
    final var mergeInto = array[0];
    return Arrays.stream(array, 1, array.length)
        .map(accountInfo -> mergeStakeAccounts(mergeInto.pubKey(), accountInfo.pubKey()))
        .toList();
  }

  @Override
  public Instruction withdrawStakeAccount(final PublicKey stakeAccount,
                                          final AccountMeta lockupAuthority,
                                          final long lamports) {
    return nativeProgramClient.withdrawStakeAccount(
        stakeAccount,
        owner.publicKey(),
        owner,
        lockupAuthority,
        lamports
    );
  }

  @Override
  public Instruction withdrawStakeAccount(final PublicKey stakeAccount, final long lamports) {
    return withdrawStakeAccount(stakeAccount, null, lamports);
  }

  @Override
  public List<Instruction> closeStakeAccounts(final Collection<AccountInfo<StakeAccount>> stakeAccounts) {
    return stakeAccounts.stream().map(this::closeStakeAccount).toList();
  }

  @Override
  public ProgramDerivedAddress findLookupTableAddress(final long recentSlot) {
    return AddressLookupTableProgram.findLookupTableAddress(accounts, owner.publicKey(), recentSlot);
  }

  @Override
  public Instruction createLookupTable(final ProgramDerivedAddress uninitializedTableAccount, final long recentSlot) {
    return AddressLookupTableProgram.createLookupTable(
        accounts,
        uninitializedTableAccount.publicKey(),
        owner,
        owner,
        recentSlot,
        uninitializedTableAccount.nonce()
    );
  }

  @Override
  public Instruction freezeLookupTable(final PublicKey tableAccount) {
    return AddressLookupTableProgram.freezeLookupTable(
        accounts,
        tableAccount,
        owner
    );
  }

  @Override
  public Instruction extendLookupTable(final PublicKey tableAccount, final List<PublicKey> newAddresses) {
    return AddressLookupTableProgram.extendLookupTable(
        accounts,
        tableAccount,
        owner,
        owner,
        newAddresses
    );
  }

  @Override
  public Instruction deactivateLookupTable(final PublicKey tableAccount) {
    return AddressLookupTableProgram.deactivateLookupTable(
        accounts,
        tableAccount,
        owner
    );
  }

  @Override
  public Instruction closeLookupTable(final PublicKey tableAccount) {
    return AddressLookupTableProgram.closeLookupTable(
        accounts,
        tableAccount,
        owner,
        owner.publicKey()
    );
  }
}
