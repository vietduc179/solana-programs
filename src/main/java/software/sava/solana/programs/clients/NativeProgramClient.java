package software.sava.solana.programs.clients;

import software.sava.core.accounts.AccountWithSeed;
import software.sava.core.accounts.PublicKey;
import software.sava.core.accounts.SolanaAccounts;
import software.sava.core.accounts.lookup.AddressLookupTable;
import software.sava.core.accounts.meta.AccountMeta;
import software.sava.core.accounts.sysvar.Clock;
import software.sava.core.tx.Instruction;
import software.sava.rpc.json.http.client.SolanaRpcClient;
import software.sava.rpc.json.http.response.AccountInfo;
import software.sava.solana.programs.stake.LockUp;
import software.sava.solana.programs.stake.StakeAccount;
import software.sava.solana.programs.stake.StakeProgram;
import software.sava.solana.programs.stake.StakeState;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;

public interface NativeProgramClient {

  static NativeProgramClient createClient(final SolanaAccounts programs) {
    return new NativeProgramClientImpl(programs);
  }

  static NativeProgramClient createClient() {
    return createClient(SolanaAccounts.MAIN_NET);
  }

  SolanaAccounts accounts();

  NativeProgramAccountClient createAccountClient(final AccountMeta ownerAndFeePayer);

  NativeProgramAccountClient createAccountClient(final PublicKey owner, final AccountMeta feePayer);

  CompletableFuture<AccountInfo<Clock>> fetchClockSysVar(final SolanaRpcClient rpcClient);

  Instruction computeUnitLimit(final int computeUnitLimit);

  Instruction computeUnitPrice(final long computeUnitPrice);

  Instruction allocateAccountSpace(final PublicKey newAccountPublicKey, final long space);

  Instruction syncNative(final PublicKey tokenAccount);

  Instruction allocateStakeAccount(final PublicKey newAccountPublicKey);

  CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsByStakeAuthority(final SolanaRpcClient rpcClient,
                                                                                        final StakeState stakeState,
                                                                                        final PublicKey staker);

  default CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsByStakeAuthority(final SolanaRpcClient rpcClient,
                                                                                                final PublicKey staker) {
    return fetchStakeAccountsByStakeAuthority(rpcClient, StakeState.Stake, staker);
  }

  CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsByWithdrawAuthority(final SolanaRpcClient rpcClient,
                                                                                           final StakeState stakeState,
                                                                                           final PublicKey withdrawer);

  default CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsByWithdrawAuthority(final SolanaRpcClient rpcClient,
                                                                                                   final PublicKey withdrawer) {
    return fetchStakeAccountsByWithdrawAuthority(rpcClient, StakeState.Stake, withdrawer);
  }

  CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsByStakeAndWithdrawAuthority(final SolanaRpcClient rpcClient,
                                                                                                   final StakeState stakeState,
                                                                                                   final PublicKey withdrawer);

  default CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsByStakeAndWithdrawAuthority(final SolanaRpcClient rpcClient,
                                                                                                           final PublicKey withdrawer) {
    return fetchStakeAccountsByStakeAndWithdrawAuthority(rpcClient, StakeState.Stake, withdrawer);
  }

  CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsWithCustodian(final SolanaRpcClient rpcClient,
                                                                                     final StakeState stakeState,
                                                                                     final PublicKey custodian);

  default CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsWithCustodian(final SolanaRpcClient rpcClient,
                                                                                             final PublicKey custodian) {
    return fetchStakeAccountsWithCustodian(rpcClient, StakeState.Stake, custodian);
  }

  CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsForValidator(final SolanaRpcClient rpcClient,
                                                                                    final StakeState stakeState,
                                                                                    final PublicKey voteAccount);

  default CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsForValidator(final SolanaRpcClient rpcClient,
                                                                                            final PublicKey voteAccount) {
    return fetchStakeAccountsForValidator(rpcClient, StakeState.Stake, voteAccount);
  }

  CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsForValidatorAndWithdrawAuthority(final SolanaRpcClient rpcClient,
                                                                                                        final StakeState stakeState,
                                                                                                        final PublicKey voteAccount,
                                                                                                        final PublicKey withdrawAuthority);

  default CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsForValidatorAndWithdrawAuthority(final SolanaRpcClient rpcClient,
                                                                                                                final PublicKey voteAccount,
                                                                                                                final PublicKey withdrawAuthority) {
    return fetchStakeAccountsForValidatorAndWithdrawAuthority(rpcClient, StakeState.Stake, voteAccount, withdrawAuthority);
  }

  CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsForValidatorAndStakeAndWithdrawAuthority(final SolanaRpcClient rpcClient,
                                                                                                                final StakeState stakeState,
                                                                                                                final PublicKey voteAccount,
                                                                                                                final PublicKey withdrawAuthority);

  default CompletableFuture<List<AccountInfo<StakeAccount>>> fetchStakeAccountsForValidatorAndStakeAndWithdrawAuthority(final SolanaRpcClient rpcClient,
                                                                                                                        final PublicKey voteAccount,
                                                                                                                        final PublicKey withdrawAuthority) {
    return fetchStakeAccountsForValidatorAndStakeAndWithdrawAuthority(rpcClient, StakeState.Stake, voteAccount, withdrawAuthority);
  }

  CompletableFuture<List<AccountInfo<AddressLookupTable>>> fetchLookupTableAccountsByAuthority(final SolanaRpcClient rpcClient,
                                                                                               final PublicKey authority);

  Instruction deactivateDelinquentStake(final PublicKey delegatedStakeAccount,
                                        final PublicKey delinquentVoteAccount,
                                        final PublicKey referenceVoteAccount);

  Instruction setStakeAccountLockup(final PublicKey initializedStakeAccount,
                                    final PublicKey lockupOrWithdrawAuthority,
                                    final Instant timestamp,
                                    final OptionalLong epoch,
                                    final PublicKey custodian);

  Instruction setStakeAccountLockupChecked(final PublicKey initializedStakeAccount,
                                           final PublicKey lockupOrWithdrawAuthority,
                                           final Instant timestamp,
                                           final OptionalLong epoch);

  Instruction setStakeAccountLockupChecked(final PublicKey initializedStakeAccount,
                                           final PublicKey lockupOrWithdrawAuthority,
                                           final PublicKey newLockupAuthority,
                                           final Instant timestamp,
                                           final OptionalLong epoch);

  Instruction authorizeStakeAccount(final PublicKey stakeAccount,
                                    final PublicKey stakeOrWithdrawAuthority,
                                    final PublicKey lockupAuthority,
                                    final PublicKey newAuthority,
                                    final StakeProgram.StakeAuthorize stakeAuthorize);

  Instruction authorizeStakeAccount(final PublicKey stakeAccount,
                                    final PublicKey stakeOrWithdrawAuthority,
                                    final PublicKey newAuthority,
                                    final StakeProgram.StakeAuthorize stakeAuthorize);

  Instruction authorizeStakeAccountChecked(final PublicKey stakeAccount,
                                           final PublicKey stakeOrWithdrawAuthority,
                                           final PublicKey newStakeOrWithdrawAuthority,
                                           final PublicKey lockupAuthority,
                                           final StakeProgram.StakeAuthorize stakeAuthorize);

  Instruction authorizeStakeAccountChecked(final PublicKey stakeAccount,
                                           final PublicKey stakeOrWithdrawAuthority,
                                           final PublicKey newStakeOrWithdrawAuthority,
                                           final StakeProgram.StakeAuthorize stakeAuthorize);

  Instruction authorizeStakeAccountWithSeed(final PublicKey stakeAccount,
                                            final AccountWithSeed baseKeyOrWithdrawAuthority,
                                            final PublicKey lockupAuthority,
                                            final PublicKey newAuthorizedPublicKey,
                                            final StakeProgram.StakeAuthorize stakeAuthorize,
                                            final PublicKey authorityOwner);

  Instruction authorizeStakeAccountWithSeed(final PublicKey stakeAccount,
                                            final AccountWithSeed baseKeyOrWithdrawAuthority,
                                            final PublicKey newAuthorizedPublicKey,
                                            final StakeProgram.StakeAuthorize stakeAuthorize,
                                            final PublicKey authorityOwner);

  Instruction authorizeStakeAccountCheckedWithSeed(final PublicKey stakeAccount,
                                                   final AccountWithSeed baseKeyOrWithdrawAuthority,
                                                   final PublicKey stakeOrWithdrawAuthority,
                                                   final PublicKey lockupAuthority,
                                                   final StakeProgram.StakeAuthorize stakeAuthorize,
                                                   final PublicKey authorityOwner);

  Instruction initializeStakeAccount(final PublicKey unInitializedStakeAccount,
                                     final PublicKey staker,
                                     final PublicKey withdrawer,
                                     final LockUp lockUp);

  Instruction initializeStakeAccount(final PublicKey unInitializedStakeAccount,
                                     final PublicKey staker,
                                     final PublicKey withdrawer);

  Instruction initializeStakeAccountChecked(final PublicKey unInitializedStakeAccount,
                                            final PublicKey staker,
                                            final PublicKey withdrawer);

  Instruction splitStakeAccount(final StakeAccount splitStakeAccount,
                                final PublicKey unInitializedStakeAccount,
                                final long lamports);

  Instruction mergeStakeAccounts(final StakeAccount destinationStakeAccount,
                                 final PublicKey srcStakeAccount);

  Instruction withdrawStakeAccount(final StakeAccount stakeAccount,
                                   final PublicKey recipient,
                                   final long lamports);

  Instruction deactivateStakeAccount(final StakeAccount delegatedStakeAccount);

  List<Instruction> deactivateStakeAccountInfos(final Collection<AccountInfo<StakeAccount>> delegatedStakeAccounts);

  List<Instruction> deactivateStakeAccounts(final Collection<StakeAccount> delegatedStakeAccounts);
}
