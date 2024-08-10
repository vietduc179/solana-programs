package software.sava.solana.programs.clients;

import software.sava.core.accounts.*;
import software.sava.core.accounts.lookup.AddressLookupTable;
import software.sava.core.accounts.meta.AccountMeta;
import software.sava.core.accounts.meta.LookupTableAccountMeta;
import software.sava.core.accounts.token.TokenAccount;
import software.sava.core.tx.Instruction;
import software.sava.core.tx.Transaction;
import software.sava.solana.programs.stake.StakeAccount;
import software.sava.solana.programs.stake.StakeProgram;
import software.sava.solana.programs.stake.StakeState;
import software.sava.rpc.json.http.client.SolanaRpcClient;
import software.sava.rpc.json.http.response.AccountInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface NativeProgramAccountClient {

  static NativeProgramAccountClient createClient(final SolanaAccounts accounts, final AccountMeta owner) {
    return new NativeProgramAccountClientImpl(accounts, owner);
  }

  static NativeProgramAccountClient createClient(final SolanaAccounts accounts, final PublicKey owner) {
    return createClient(accounts, AccountMeta.createWritableSigner(owner));
  }

  static NativeProgramAccountClient createClient(final SolanaAccounts accounts, final Signer owner) {
    return createClient(accounts, AccountMeta.createFeePayer(owner.publicKey()));
  }

  static NativeProgramAccountClient createClient(final AccountMeta owner) {
    return createClient(SolanaAccounts.MAIN_NET, owner);
  }

  static NativeProgramAccountClient createClient(final PublicKey owner) {
    return createClient(SolanaAccounts.MAIN_NET, AccountMeta.createWritableSigner(owner));
  }

  static NativeProgramAccountClient createClient(final Signer owner) {
    return createClient(SolanaAccounts.MAIN_NET, AccountMeta.createFeePayer(owner.publicKey()));
  }

  static CompletableFuture<Long> getMinimumBalanceForStakeAccount(final SolanaRpcClient rpcClient) {
    return rpcClient.getMinimumBalanceForRentExemption(StakeAccount.BYTES);
  }

  AccountMeta owner();

  default PublicKey ownerPublicKey() {
    return owner().publicKey();
  }

  SolanaAccounts accounts();

  ProgramDerivedAddress wrappedSolPDA();

  NativeProgramClient nativeProgramClient();

  Transaction createTransaction(final PublicKey feePayer, final Instruction instruction);

  Transaction createTransaction(final PublicKey feePayer, final List<Instruction> instructions);

  Transaction createTransaction(final AccountMeta feePayer, final Instruction instruction);

  Transaction createTransaction(final AccountMeta feePayer, final List<Instruction> instructions);

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
                                final Instruction instruction,
                                final AddressLookupTable lookupTable);

  Transaction createTransaction(final PublicKey feePayer,
                                final List<Instruction> instructions,
                                final AddressLookupTable lookupTable);

  Transaction createTransaction(final AccountMeta feePayer,
                                final Instruction instruction,
                                final AddressLookupTable lookupTable);

  Transaction createTransaction(final AccountMeta feePayer,
                                final List<Instruction> instructions,
                                final AddressLookupTable lookupTable);

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
                                final Instruction instruction,
                                final LookupTableAccountMeta[] tableAccountMetas);

  Transaction createTransaction(final PublicKey feePayer,
                                final List<Instruction> instructions,
                                final LookupTableAccountMeta[] tableAccountMetas);

  Transaction createTransaction(final AccountMeta feePayer,
                                final Instruction instruction,
                                final LookupTableAccountMeta[] tableAccountMetas);

  Transaction createTransaction(final AccountMeta feePayer,
                                final List<Instruction> instructions,
                                final LookupTableAccountMeta[] tableAccountMetas);

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

  AccountWithSeed createOffCurveAccountWithSeed(final String asciiSeed);

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
                                          final long lamports,
                                          final PublicKey programOwner);

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

  Instruction deactivateStakeAccount(final PublicKey delegatedStakeAccount);

  Instruction initializeStakeAccountWithStaker(final PublicKey unInitializedStakeAccount,
                                               final PublicKey staker);

  Instruction initializeStakeAccountWithWithdrawer(final PublicKey unInitializedStakeAccount,
                                                   final PublicKey withdrawer);

  Instruction initializeStakeAccount(final PublicKey unInitializedStakeAccount);

  Instruction initializeStakeAccountChecked(final PublicKey unInitializedStakeAccount,
                                            final PublicKey staker);

  Instruction initializeStakeAccountChecked(final PublicKey unInitializedStakeAccount,
                                            final AccountMeta withdrawer);

  Instruction initializeStakeAccountChecked(final PublicKey unInitializedStakeAccount);

  Instruction authorizeStakeAccount(final PublicKey stakeAccount,
                                    final PublicKey newAuthority,
                                    final StakeProgram.StakeAuthorize stakeAuthorize);

  Instruction delegateStakeAccount(final PublicKey initializedStakeAccount,
                                   final PublicKey validatorVoteAccount);

  Instruction reDelegateStakeAccount(final PublicKey delegatedStakeAccount,
                                     final PublicKey uninitializedStakeAccount,
                                     final PublicKey validatorVoteAccount);

  Instruction splitStakeAccount(final PublicKey splitStakeAccount,
                                final PublicKey unInitializedStakeAccount,
                                final long lamports);

  Instruction mergeStakeAccounts(final PublicKey destinationStakeAccount,
                                 final PublicKey srcStakeAccount);

  Instruction withdrawStakeAccount(final PublicKey stakeAccount,
                                   final AccountMeta lockupAuthority,
                                   final long lamports);

  Instruction withdrawStakeAccount(final PublicKey stakeAccount, final long lamports);

  default Instruction withdrawStakeAccount(final StakeAccount stakeAccount) {
    return withdrawStakeAccount(stakeAccount.address(), stakeAccount.stake());
  }

  ProgramDerivedAddress findLookupTableAddress(final long recentSlot);

  Instruction createLookupTable(final ProgramDerivedAddress uninitializedTableAccount, final long recentSlot);

  Instruction freezeLookupTable(final PublicKey tableAccount);

  Instruction extendLookupTable(final PublicKey tableAccount, final List<PublicKey> newAddresses);

  Instruction deactivateLookupTable(final PublicKey tableAccount);

  Instruction closeLookupTable(final PublicKey tableAccount);
}
