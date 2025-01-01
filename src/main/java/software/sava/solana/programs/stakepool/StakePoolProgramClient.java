package software.sava.solana.programs.stakepool;

import software.sava.core.accounts.ProgramDerivedAddress;
import software.sava.core.accounts.PublicKey;
import software.sava.core.accounts.SolanaAccounts;
import software.sava.core.tx.Instruction;
import software.sava.rpc.json.http.client.SolanaRpcClient;
import software.sava.rpc.json.http.response.AccountInfo;
import software.sava.solana.programs.clients.NativeProgramAccountClient;

import java.util.concurrent.CompletableFuture;

public interface StakePoolProgramClient {

  static StakePoolProgramClient createClient(final NativeProgramAccountClient nativeProgramClient,
                                             final StakePoolAccounts stakePoolAccounts) {
    return new StakePoolProgramClientImpl(nativeProgramClient, stakePoolAccounts);
  }

  static StakePoolProgramClient createClient(final NativeProgramAccountClient nativeProgramClient) {
    return createClient(nativeProgramClient, StakePoolAccounts.MAIN_NET);
  }

  NativeProgramAccountClient nativeProgramAccountClient();

  SolanaAccounts solanaAccounts();

  StakePoolAccounts stakePoolAccounts();

  static CompletableFuture<AccountInfo<StakePoolState>> fetchProgramState(final SolanaRpcClient rpcClient,
                                                                          final PublicKey stakePoolPublicKey) {
    return rpcClient.getAccountInfo(stakePoolPublicKey, StakePoolState.FACTORY);
  }

  static CompletableFuture<AccountInfo<ValidatorList>> fetchValidatorList(final SolanaRpcClient rpcClient,
                                                                          final StakePoolState programState) {
    final var destinationValidatorList = programState.validatorList();
    return rpcClient.getAccountInfo(destinationValidatorList, ValidatorList.FACTORY);
  }

  static ProgramDerivedAddress findStakePoolWithdrawAuthority(final AccountInfo<StakePoolState> stakePoolStateAccountInfo) {
    return StakePoolProgram.findStakePoolWithdrawAuthority(stakePoolStateAccountInfo.pubKey(), stakePoolStateAccountInfo.owner());
  }

  PublicKey ownerPublicKey();

  Instruction depositSol(final AccountInfo<StakePoolState> stakePoolStateAccountInfo,
                         final PublicKey poolTokenATA,
                         final long lamportsIn);

  Instruction depositSolWithSlippage(final AccountInfo<StakePoolState> stakePoolStateAccountInfo,
                                     final PublicKey poolTokenATA,
                                     final long lamportsIn,
                                     final long minimumPoolTokensOut);

  Instruction depositStake(final AccountInfo<StakePoolState> stakePoolStateAccountInfo,
                           final PublicKey depositStakeAccount,
                           final PublicKey validatorStakeAccount,
                           final PublicKey poolTokenATA);

  Instruction depositStakeWithSlippage(final AccountInfo<StakePoolState> stakePoolStateAccountInfo,
                                       final PublicKey depositStakeAccount,
                                       final PublicKey validatorStakeAccount,
                                       final PublicKey poolTokenATA,
                                       final long minimumPoolTokensOut);

  Instruction withdrawSol(final PublicKey stakePoolProgram,
                          final StakePoolState stakePoolState,
                          final PublicKey poolTokenATA,
                          final long poolTokenAmount);

  default Instruction withdrawSol(final AccountInfo<StakePoolState> stakePoolStateAccountInfo,
                                  final PublicKey poolTokenATA,
                                  final long poolTokenAmount) {
    final var stakePoolState = stakePoolStateAccountInfo.data();
    return withdrawSol(
        stakePoolStateAccountInfo.owner(),
        stakePoolState,
        poolTokenATA,
        poolTokenAmount
    );
  }

  Instruction withdrawSolWithSlippage(final AccountInfo<StakePoolState> stakePoolStateAccountInfo,
                                      final PublicKey poolTokenATA,
                                      final long poolTokenAmount,
                                      final long lamportsOut);

  Instruction withdrawStake(final PublicKey poolProgram,
                            final StakePoolState stakePoolState,
                            final PublicKey validatorOrReserveStakeAccount,
                            final PublicKey uninitializedStakeAccount,
                            final PublicKey stakeAccountWithdrawalAuthority,
                            final PublicKey poolTokenATA,
                            final long poolTokenAmount);

  default Instruction withdrawStake(final AccountInfo<StakePoolState> stakePoolStateAccountInfo,
                                    final PublicKey validatorOrReserveStakeAccount,
                                    final PublicKey uninitializedStakeAccount,
                                    final PublicKey stakeAccountWithdrawalAuthority,
                                    final PublicKey poolTokenATA,
                                    final long poolTokenAmount) {
    final var stakePoolState = stakePoolStateAccountInfo.data();
    return withdrawStake(
        stakePoolStateAccountInfo.owner(),
        stakePoolState,
        validatorOrReserveStakeAccount,
        uninitializedStakeAccount,
        stakeAccountWithdrawalAuthority,
        poolTokenATA,
        poolTokenAmount
    );
  }

  Instruction withdrawStakeWithSlippage(final AccountInfo<StakePoolState> stakePoolStateAccountInfo,
                                        final PublicKey validatorOrReserveStakeAccount,
                                        final PublicKey uninitializedStakeAccount,
                                        final PublicKey stakeAccountWithdrawalAuthority,
                                        final PublicKey poolTokenATA,
                                        final long poolTokenAmount,
                                        final long lamportsOut);

  Instruction withdrawStakeWithSlippage(final AccountInfo<StakePoolState> stakePoolStateAccountInfo,
                                        final PublicKey validatorOrReserveStakeAccount,
                                        final PublicKey uninitializedStakeAccount,
                                        final PublicKey poolTokenATA,
                                        final long poolTokenAmount,
                                        final long lamportsOut);

  Instruction updateStakePoolBalance(final AccountInfo<StakePoolState> stakePoolStateAccountInfo);
}
