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
import software.sava.solana.programs.stake.StakeState;
import software.sava.solana.programs.system.SystemProgram;
import software.sava.solana.programs.token.AssociatedTokenProgram;
import software.sava.solana.programs.token.TokenProgram;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static software.sava.solana.programs.compute_budget.ComputeBudgetProgram.COMPUTE_UNITS_CONSUMED;

final class NativeProgramAccountClientImpl implements NativeProgramAccountClient {

  private final SolanaAccounts accounts;
  private final NativeProgramClient nativeProgramClient;
  private final PublicKey owner;
  private final AccountMeta feePayer;
  private final ProgramDerivedAddress wrappedSolPDA;

  NativeProgramAccountClientImpl(final SolanaAccounts accounts,
                                 final PublicKey owner,
                                 final AccountMeta feePayer) {
    this.accounts = accounts;
    this.nativeProgramClient = NativeProgramClient.createClient(accounts);
    this.owner = owner;
    this.wrappedSolPDA = findAssociatedTokenProgramAddress(accounts.wrappedSolTokenMint());
    this.feePayer = feePayer;
  }

  NativeProgramAccountClientImpl(final NativeProgramClient nativeProgramClient,
                                 final PublicKey owner,
                                 final AccountMeta feePayer) {
    this.accounts = nativeProgramClient.accounts();
    this.nativeProgramClient = nativeProgramClient;
    this.owner = owner;
    this.feePayer = feePayer;
    this.wrappedSolPDA = findAssociatedTokenProgramAddress(accounts.wrappedSolTokenMint());
  }

  @Override
  public PublicKey ownerPublicKey() {
    return owner;
  }

  @Override
  public AccountMeta feePayer() {
    return feePayer;
  }

  @Override
  public SolanaAccounts solanaAccounts() {
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
    return Transaction.createTx(feePayer, instruction);
  }

  @Override
  public Transaction createTransaction(final List<Instruction> instructions) {
    return Transaction.createTx(feePayer, instructions);
  }

  @Override
  public Transaction createTransaction(final int computeUnitLimit,
                                       final long microLamportComputeUnitPrice,
                                       final Instruction instruction) {
    return createTransaction(feePayer, computeUnitLimit, microLamportComputeUnitPrice, instruction);
  }

  @Override
  public Transaction createTransaction(final int computeUnitLimit,
                                       final long microLamportComputeUnitPrice,
                                       final List<Instruction> instructions) {
    return createTransaction(computeUnitLimit, microLamportComputeUnitPrice, Transaction.createTx(feePayer, instructions));
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
    return Transaction.createTx(feePayer, List.of(instruction), lookupTable);
  }

  @Override
  public Transaction createTransaction(final List<Instruction> instructions, final AddressLookupTable lookupTable) {
    return Transaction.createTx(feePayer, instructions, lookupTable);
  }

  @Override
  public Transaction createTransaction(final int computeUnitLimit,
                                       final long microLamportComputeUnitPrice,
                                       final Instruction instruction,
                                       final AddressLookupTable lookupTable) {
    return createTransaction(feePayer, computeUnitLimit, microLamportComputeUnitPrice, instruction, lookupTable);
  }


  @Override
  public Transaction createTransaction(final int computeUnitLimit,
                                       final long microLamportComputeUnitPrice,
                                       final List<Instruction> instructions,
                                       final AddressLookupTable lookupTable) {
    return createTransaction(
        computeUnitLimit,
        microLamportComputeUnitPrice,
        Transaction.createTx(feePayer, instructions, lookupTable)
    );
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
    return Transaction.createTx(feePayer, List.of(instruction), tableAccountMetas);
  }

  @Override
  public Transaction createTransaction(final List<Instruction> instructions, final LookupTableAccountMeta[] tableAccountMetas) {
    return Transaction.createTx(feePayer, instructions, tableAccountMetas);
  }

  @Override
  public Transaction createTransaction(final int computeUnitLimit,
                                       final long microLamportComputeUnitPrice,
                                       final Instruction instruction,
                                       final LookupTableAccountMeta[] tableAccountMetas) {
    return createTransaction(feePayer, computeUnitLimit, microLamportComputeUnitPrice, instruction, tableAccountMetas);
  }

  @Override
  public Transaction createTransaction(final int computeUnitLimit,
                                       final long microLamportComputeUnitPrice,
                                       final List<Instruction> instructions,
                                       final LookupTableAccountMeta[] tableAccountMetas) {
    return createTransaction(
        computeUnitLimit,
        microLamportComputeUnitPrice,
        Transaction.createTx(feePayer, instructions, tableAccountMetas)
    );
  }

  @Override
  public ProgramDerivedAddress findAssociatedTokenProgramAddress(final PublicKey mint) {
    return AssociatedTokenProgram.findAssociatedTokenProgramAddress(accounts, owner, mint);
  }

  @Override
  public CompletableFuture<List<AccountInfo<TokenAccount>>> fetchTokenAccounts(final SolanaRpcClient rpcClient, final PublicKey tokenMintAddress) {
    return rpcClient.getTokenAccountsForTokenMintByOwner(owner, tokenMintAddress);
  }

  @Override
  public CompletableFuture<List<AccountInfo<TokenAccount>>> fetchTokenAccounts(final SolanaRpcClient rpcClient) {
    return rpcClient.getTokenAccountsForProgramByOwner(owner, accounts.tokenProgram());
  }

  @Override
  public CompletableFuture<List<AccountInfo<TokenAccount>>> fetchToken2022Accounts(final SolanaRpcClient rpcClient) {
    return rpcClient.getTokenAccountsForProgramByOwner(owner, accounts.token2022Program());
  }

  @Override
  public Instruction syncNative() {
    return nativeProgramClient.syncNative(wrappedSolPDA.publicKey());
  }

  @Override
  public List<Instruction> wrapSOL(final long lamports) {
    final var createATAIx = createATA(true, wrappedSolPDA.publicKey(), accounts.wrappedSolTokenMint());
    final var transferIx = transferSolLamports(wrappedSolPDA.publicKey(), lamports);
    final var syncNativeIx = nativeProgramClient.syncNative(wrappedSolPDA.publicKey());
    return List.of(createATAIx, transferIx, syncNativeIx);
  }

  @Override
  public Instruction unwrapSOL() {
    return closeTokenAccount(wrappedSolPDA.publicKey());
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
        space,
        programOwner
    );
  }

  @Override
  public AccountWithSeed createOffCurveAccountWithSeed(final String asciiSeed, final PublicKey programId) {
    return PublicKey.createOffCurveAccountWithAsciiSeed(owner, asciiSeed, programId);
  }

  @Override
  public AccountWithSeed createOffCurveStakeAccountWithSeed(final String asciiSeed) {
    return createOffCurveAccountWithSeed(asciiSeed, accounts.stakeProgram());
  }

  @Override
  public CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsByStakeAuthority(final SolanaRpcClient rpcClient,
                                                                                               final StakeState stakeState) {
    return nativeProgramClient.fetchStakeAccountsByStakeAuthority(rpcClient, stakeState, ownerPublicKey());
  }

  @Override
  public CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsByWithdrawAuthority(final SolanaRpcClient rpcClient,
                                                                                                  final StakeState stakeState) {
    return nativeProgramClient.fetchStakeAccountsByWithdrawAuthority(rpcClient, stakeState, ownerPublicKey());
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
                                                 final PublicKey recipientAccount,
                                                 final long lamports,
                                                 final PublicKey programOwner) {
    return SystemProgram.transferWithSeed(
        accounts.invokedSystemProgram(),
        accountWithSeed,
        recipientAccount,
        lamports,
        programOwner
    );
  }

  @Override
  public Instruction transferSolLamports(final PublicKey toPublicKey, final long lamports) {
    return SystemProgram.transfer(
        accounts.invokedSystemProgram(),
        owner,
        toPublicKey,
        lamports
    );
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
        owner,
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
    return createATAFor(idempotent, owner, programDerivedAddress, mint);
  }

  @Override
  public Instruction createATA(final boolean idempotent, final PublicKey mint) {
    return createATAFor(idempotent, owner, mint);
  }

  @Override
  public Instruction initializeStakeAccount(final PublicKey unInitializedStakeAccount,
                                            final PublicKey staker) {
    return nativeProgramClient.initializeStakeAccount(
        unInitializedStakeAccount,
        staker,
        owner
    );
  }

  @Override
  public Instruction initializeStakeAccount(final PublicKey unInitializedStakeAccount) {
    return initializeStakeAccount(unInitializedStakeAccount, owner);
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
  public Instruction initializeStakeAccountChecked(final PublicKey unInitializedStakeAccount) {
    return nativeProgramClient.initializeStakeAccountChecked(
        unInitializedStakeAccount,
        owner,
        owner
    );
  }

  @Override
  public Instruction withdrawStakeAccount(final StakeAccount stakeAccount, final long lamports) {
    return nativeProgramClient.withdrawStakeAccount(
        stakeAccount,
        owner,
        lamports
    );
  }

  @Override
  public ProgramDerivedAddress findLookupTableAddress(final long recentSlot) {
    return AddressLookupTableProgram.findLookupTableAddress(accounts, owner, recentSlot);
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
        owner
    );
  }
}
