package software.sava.solana.programs.clients;

import software.sava.core.accounts.*;
import software.sava.core.accounts.lookup.AddressLookupTable;
import software.sava.core.accounts.meta.AccountMeta;
import software.sava.core.accounts.meta.LookupTableAccountMeta;
import software.sava.core.accounts.token.TokenAccount;
import software.sava.core.tx.Instruction;
import software.sava.core.tx.Transaction;
import software.sava.rpc.json.http.client.SolanaRpcClient;
import software.sava.rpc.json.http.response.AccountInfo;
import software.sava.solana.programs.stake.StakeAccount;
import software.sava.solana.programs.stake.StakeState;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface NativeProgramAccountClient {

  static NativeProgramAccountClient createClient(final SolanaAccounts accounts,
                                                 final AccountMeta ownerAndFeePayer) {
    return new NativeProgramAccountClientImpl(accounts, ownerAndFeePayer.publicKey(), ownerAndFeePayer);
  }

  static NativeProgramAccountClient createClient(final SolanaAccounts accounts,
                                                 final PublicKey ownerAndFeePayer) {
    return createClient(accounts, AccountMeta.createFeePayer(ownerAndFeePayer));
  }

  static NativeProgramAccountClient createClient(final SolanaAccounts accounts,
                                                 final Signer ownerAndFeePayer) {
    return createClient(accounts, AccountMeta.createFeePayer(ownerAndFeePayer.publicKey()));
  }

  static NativeProgramAccountClient createClient(final AccountMeta ownerAndFeePayer) {
    return createClient(SolanaAccounts.MAIN_NET, ownerAndFeePayer);
  }

  static NativeProgramAccountClient createClient(final PublicKey ownerAndFeePayer) {
    return createClient(SolanaAccounts.MAIN_NET, ownerAndFeePayer);
  }

  static NativeProgramAccountClient createClient(final Signer ownerAndFeePayer) {
    return createClient(SolanaAccounts.MAIN_NET, ownerAndFeePayer);
  }

  static NativeProgramAccountClient createClient(final SolanaAccounts accounts,
                                                 final PublicKey owner,
                                                 final AccountMeta feePayer) {
    return new NativeProgramAccountClientImpl(accounts, owner, feePayer);
  }

  static NativeProgramAccountClient createClient(final SolanaAccounts accounts,
                                                 final PublicKey owner,
                                                 final PublicKey feePayer) {
    return createClient(accounts, owner, AccountMeta.createFeePayer(feePayer));
  }

  static NativeProgramAccountClient createClient(final SolanaAccounts accounts,
                                                 final PublicKey owner,
                                                 final Signer feePayer) {
    return createClient(accounts, owner, AccountMeta.createFeePayer(feePayer.publicKey()));
  }

  static NativeProgramAccountClient createClient(final PublicKey owner, final AccountMeta feePayer) {
    return createClient(SolanaAccounts.MAIN_NET, owner, feePayer);
  }

  static NativeProgramAccountClient createClient(final PublicKey owner, final PublicKey feePayer) {
    return createClient(SolanaAccounts.MAIN_NET, owner, feePayer);
  }

  static NativeProgramAccountClient createClient(final PublicKey owner, final Signer feePayer) {
    return createClient(SolanaAccounts.MAIN_NET, owner, feePayer);
  }

  static CompletableFuture<Long> getMinimumBalanceForStakeAccount(final SolanaRpcClient rpcClient) {
    return rpcClient.getMinimumBalanceForRentExemption(StakeAccount.BYTES);
  }

  PublicKey ownerPublicKey();

  AccountMeta feePayer();

  SolanaAccounts solanaAccounts();

  ProgramDerivedAddress wrappedSolPDA();

  NativeProgramClient nativeProgramClient();

  Transaction createTransaction(final PublicKey feePayer,
                                final int computeUnitLimit,
                                final long microLamportComputeUnitPrice,
                                final Instruction instruction);

  Transaction createTransaction(final AccountMeta feePayer,
                                final int computeUnitLimit,
                                final long microLamportComputeUnitPrice,
                                final Instruction instruction);

  Transaction createTransaction(final Instruction instruction);

  Transaction createTransaction(final List<Instruction> instructions);

  Transaction createTransaction(final int computeUnitLimit,
                                final long microLamportComputeUnitPrice,
                                final Instruction instruction);

  Transaction createTransaction(final int computeUnitLimit,
                                final long microLamportComputeUnitPrice,
                                final List<Instruction> instructions);

  Transaction createTransaction(final int computeUnitLimit,
                                final long microLamportComputeUnitPrice,
                                final Transaction instruction);

  Transaction createTransaction(final PublicKey feePayer,
                                final int computeUnitLimit,
                                final long microLamportComputeUnitPrice,
                                final Instruction instruction,
                                final AddressLookupTable lookupTable);

  Transaction createTransaction(final AccountMeta feePayer,
                                final int computeUnitLimit,
                                final long microLamportComputeUnitPrice,
                                final Instruction instruction,
                                final AddressLookupTable lookupTable);

  Transaction createTransaction(final Instruction instruction,
                                final AddressLookupTable lookupTable);

  Transaction createTransaction(final List<Instruction> instructions,
                                final AddressLookupTable lookupTable);

  Transaction createTransaction(final int computeUnitLimit,
                                final long microLamportComputeUnitPrice,
                                final Instruction instruction,
                                final AddressLookupTable lookupTable);

  Transaction createTransaction(final int computeUnitLimit,
                                final long microLamportComputeUnitPrice,
                                final List<Instruction> instructions,
                                final AddressLookupTable lookupTable);

  Transaction createTransaction(final PublicKey feePayer,
                                final int computeUnitLimit,
                                final long microLamportComputeUnitPrice,
                                final Instruction instruction,
                                final LookupTableAccountMeta[] tableAccountMetas);

  Transaction createTransaction(final AccountMeta feePayer,
                                final int computeUnitLimit,
                                final long microLamportComputeUnitPrice,
                                final Instruction instruction,
                                final LookupTableAccountMeta[] tableAccountMetas);

  Transaction createTransaction(final Instruction instruction,
                                final LookupTableAccountMeta[] tableAccountMetas);

  Transaction createTransaction(final List<Instruction> instructions,
                                final LookupTableAccountMeta[] tableAccountMetas);

  Transaction createTransaction(final int computeUnitLimit,
                                final long microLamportComputeUnitPrice,
                                final Instruction instruction,
                                final LookupTableAccountMeta[] tableAccountMetas);

  Transaction createTransaction(final int computeUnitLimit,
                                final long microLamportComputeUnitPrice,
                                final List<Instruction> instructions,
                                final LookupTableAccountMeta[] tableAccountMetas);

  ProgramDerivedAddress findAssociatedTokenProgramAddress(final PublicKey mint);

  CompletableFuture<List<AccountInfo<TokenAccount>>> fetchTokenAccounts(final SolanaRpcClient rpcClient,
                                                                        final PublicKey tokenMintAddress);

  CompletableFuture<List<AccountInfo<TokenAccount>>> fetchTokenAccounts(final SolanaRpcClient rpcClient);

  CompletableFuture<List<AccountInfo<TokenAccount>>> fetchToken2022Accounts(final SolanaRpcClient rpcClient);

  Instruction syncNative();

  List<Instruction> wrapSOL(final long lamports);

  Instruction unwrapSOL();

  Instruction createAccount(final PublicKey newAccountPublicKey,
                            final long lamports,
                            final long space,
                            final PublicKey programOwner);

  Instruction createAccountWithSeed(final AccountWithSeed accountWithSeed,
                                    final long lamports,
                                    final long space,
                                    final PublicKey programOwner);

  Instruction transferSolLamports(final PublicKey toPublicKey, final long lamports);

  Instruction allocateAccountSpaceWithSeed(final AccountWithSeed accountWithSeed,
                                           final long space,
                                           final PublicKey programOwner);

  AccountWithSeed createOffCurveAccountWithSeed(final String asciiSeed, final PublicKey programId);

  AccountWithSeed createOffCurveStakeAccountWithSeed(final String asciiSeed);

  CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsByStakeAuthority(final SolanaRpcClient rpcClient,
                                                                                        final StakeState stakeState);

  default CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsByStakeAuthority(final SolanaRpcClient rpcClient) {
    return fetchStakeAccountsByStakeAuthority(rpcClient, StakeState.Stake);
  }

  CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsByWithdrawAuthority(final SolanaRpcClient rpcClient,
                                                                                           final StakeState stakeState);

  default CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsByWithdrawAuthority(final SolanaRpcClient rpcClient) {
    return fetchStakeAccountsByWithdrawAuthority(rpcClient, StakeState.Stake);
  }

  CompletableFuture<List<AccountInfo<AddressLookupTable>>> fetchLookupTableAccountsByAuthority(final SolanaRpcClient rpcClient);

  Instruction createStakeAccount(final PublicKey newAccountPublicKey, final long lamports);

  Instruction createStakeAccountWithSeed(final AccountWithSeed accountWithSeed, final long lamports);

  Instruction allocateStakeAccountWithSeed(final AccountWithSeed accountWithSeed);

  Instruction transferSolLamportsWithSeed(final AccountWithSeed accountWithSeed,
                                          final PublicKey recipientAccount,
                                          final long lamports,
                                          final PublicKey programOwner);

  default Instruction transferSolLamportsWithSeed(final AccountWithSeed accountWithSeed,
                                                  final long lamports,
                                                  final PublicKey programOwner) {
    return transferSolLamportsWithSeed(accountWithSeed, ownerPublicKey(), lamports, programOwner);
  }

  Instruction transferToken(final PublicKey fromTokenAccount,
                            final PublicKey toTokenAccount,
                            final long lamports);

  Instruction transferTokenChecked(final PublicKey fromTokenAccount,
                                   final PublicKey toTokenAccount,
                                   final long lamports,
                                   final int decimals,
                                   final PublicKey tokenMint);

  Instruction closeTokenAccount(final PublicKey tokenAccount);

  Instruction createATAFor(final boolean idempotent,
                           final PublicKey tokenAccountOwner,
                           final PublicKey programDerivedAddress,
                           final PublicKey mint);

  Instruction createATAFor(final boolean idempotent,
                           final PublicKey tokenAccountOwner,
                           final PublicKey mint);

  Instruction createATA(final boolean idempotent, final PublicKey programDerivedAddress, final PublicKey mint);

  Instruction createATA(final boolean idempotent, final PublicKey mint);

  Instruction initializeStakeAccount(final PublicKey unInitializedStakeAccount,
                                     final PublicKey staker);

  Instruction initializeStakeAccount(final PublicKey unInitializedStakeAccount);

  Instruction initializeStakeAccountChecked(final PublicKey unInitializedStakeAccount,
                                            final PublicKey staker);

  Instruction initializeStakeAccountChecked(final PublicKey unInitializedStakeAccount);
  
  Instruction deactivateStakeAccount(final StakeAccount delegatedStakeAccount);

  List<Instruction> deactivateStakeAccountInfos(final Collection<AccountInfo<StakeAccount>> delegatedStakeAccounts);

  List<Instruction> deactivateStakeAccounts(final Collection<StakeAccount> delegatedStakeAccounts);

  Instruction withdrawStakeAccount(final StakeAccount stakeAccount, final long lamports);

  default Instruction closeStakeAccount(final AccountInfo<StakeAccount> stakeAccountInfo) {
    final var stakeAccount = stakeAccountInfo.data();
    return withdrawStakeAccount(stakeAccount, stakeAccountInfo.lamports() + stakeAccount.stake());
  }

  default List<Instruction> closeStakeAccounts(final Collection<AccountInfo<StakeAccount>> stakeAccounts) {
    return stakeAccounts.stream().map(this::closeStakeAccount).toList();
  }

  ProgramDerivedAddress findLookupTableAddress(final long recentSlot);

  Instruction createLookupTable(final ProgramDerivedAddress uninitializedTableAccount, final long recentSlot);

  Instruction freezeLookupTable(final PublicKey tableAccount);

  Instruction extendLookupTable(final PublicKey tableAccount, final List<PublicKey> newAddresses);

  Instruction deactivateLookupTable(final PublicKey tableAccount);

  Instruction closeLookupTable(final PublicKey tableAccount);
}
