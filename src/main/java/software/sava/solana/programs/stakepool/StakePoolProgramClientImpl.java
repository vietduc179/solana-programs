package software.sava.solana.programs.stakepool;

import software.sava.core.accounts.PublicKey;
import software.sava.core.accounts.SolanaAccounts;
import software.sava.core.accounts.meta.AccountMeta;
import software.sava.core.tx.Instruction;
import software.sava.rpc.json.http.response.AccountInfo;
import software.sava.solana.programs.clients.NativeProgramAccountClient;

final class StakePoolProgramClientImpl implements StakePoolProgramClient {

  private final NativeProgramAccountClient nativeProgramClient;
  private final SolanaAccounts accounts;
  private final StakePoolAccounts stakePoolAccounts;
  private final PublicKey owner;

  StakePoolProgramClientImpl(final NativeProgramAccountClient nativeProgramClient,
                             final StakePoolAccounts stakePoolAccounts) {
    this.nativeProgramClient = nativeProgramClient;
    this.accounts = nativeProgramClient.solanaAccounts();
    this.stakePoolAccounts = stakePoolAccounts;
    this.owner = nativeProgramClient.ownerPublicKey();
  }

  @Override
  public NativeProgramAccountClient nativeProgramAccountClient() {
    return nativeProgramClient;
  }

  @Override
  public StakePoolAccounts stakePoolAccounts() {
    return stakePoolAccounts;
  }

  @Override
  public Instruction depositSol(final AccountInfo<StakePoolState> stakePoolStateAccountInfo,
                                final PublicKey poolTokenATA,
                                final long lamportsIn) {
    final var stakePoolState = stakePoolStateAccountInfo.data();
    return StakePoolProgram.depositSol(
        accounts,
        AccountMeta.createInvoked(stakePoolStateAccountInfo.owner()),
        stakePoolState.address(),
        stakePoolState.reserveStake(),
        owner,
        poolTokenATA,
        stakePoolState.managerFeeAccount(),
        poolTokenATA,
        stakePoolState.poolMint(),
        stakePoolState.tokenProgramId(),
        lamportsIn
    );
  }

  @Override
  public Instruction depositSolWithSlippage(final AccountInfo<StakePoolState> stakePoolStateAccountInfo,
                                            final PublicKey poolTokenATA,
                                            final long lamportsIn,
                                            final long minimumPoolTokensOut) {
    final var stakePoolState = stakePoolStateAccountInfo.data();
    return StakePoolProgram.depositSolWithSlippage(
        accounts,
        AccountMeta.createInvoked(stakePoolStateAccountInfo.owner()),
        stakePoolState.address(),
        stakePoolState.reserveStake(),
        owner,
        poolTokenATA,
        stakePoolState.managerFeeAccount(),
        poolTokenATA,
        stakePoolState.poolMint(),
        stakePoolState.tokenProgramId(),
        lamportsIn,
        minimumPoolTokensOut
    );
  }

  @Override
  public Instruction depositStake(final AccountInfo<StakePoolState> stakePoolStateAccountInfo,
                                  final PublicKey depositStakeAccount,
                                  final PublicKey validatorStakeAccount,
                                  final PublicKey poolTokenATA) {
    final var stakePoolState = stakePoolStateAccountInfo.data();
    return StakePoolProgram.depositStake(
        accounts,
        AccountMeta.createInvoked(stakePoolStateAccountInfo.owner()),
        stakePoolState.address(),
        stakePoolState.validatorList(),
        owner,
        depositStakeAccount,
        validatorStakeAccount,
        stakePoolState.reserveStake(),
        poolTokenATA,
        stakePoolState.managerFeeAccount(),
        poolTokenATA,
        stakePoolState.poolMint(),
        stakePoolState.tokenProgramId()
    );
  }

  @Override
  public Instruction depositStakeWithSlippage(final AccountInfo<StakePoolState> stakePoolStateAccountInfo,
                                              final PublicKey depositStakeAccount,
                                              final PublicKey validatorStakeAccount,
                                              final PublicKey poolTokenATA,
                                              final long minimumPoolTokensOut) {
    final var stakePoolState = stakePoolStateAccountInfo.data();
    return StakePoolProgram.depositStakeWithSlippage(
        accounts,
        AccountMeta.createInvoked(stakePoolStateAccountInfo.owner()),
        stakePoolState.address(),
        stakePoolState.validatorList(),
        owner,
        depositStakeAccount,
        validatorStakeAccount,
        stakePoolState.reserveStake(),
        poolTokenATA,
        stakePoolState.managerFeeAccount(),
        poolTokenATA,
        stakePoolState.poolMint(),
        stakePoolState.tokenProgramId(),
        minimumPoolTokensOut
    );
  }

  @Override
  public Instruction withdrawSolWithSlippage(final AccountInfo<StakePoolState> stakePoolStateAccountInfo,
                                             final PublicKey poolTokenATA,
                                             final long poolTokenAmount,
                                             final long lamportsOut) {
    final var stakePoolState = stakePoolStateAccountInfo.data();
    return StakePoolProgram.withdrawSolWithSlippage(
        accounts,
        AccountMeta.createInvoked(stakePoolStateAccountInfo.owner()),
        stakePoolState.address(),
        owner,
        poolTokenATA,
        stakePoolState.reserveStake(),
        owner,
        stakePoolState.managerFeeAccount(),
        stakePoolState.poolMint(),
        stakePoolState.tokenProgramId(),
        poolTokenAmount,
        lamportsOut
    );
  }

  @Override
  public Instruction withdrawSol(final AccountInfo<StakePoolState> stakePoolStateAccountInfo,
                                 final PublicKey poolTokenATA,
                                 final long poolTokenAmount) {
    final var stakePoolState = stakePoolStateAccountInfo.data();
    return StakePoolProgram.withdrawSol(
        accounts,
        AccountMeta.createInvoked(stakePoolStateAccountInfo.owner()),
        stakePoolState.address(),
        owner,
        poolTokenATA,
        stakePoolState.reserveStake(),
        owner,
        stakePoolState.managerFeeAccount(),
        stakePoolState.poolMint(),
        stakePoolState.tokenProgramId(),
        poolTokenAmount
    );
  }

  @Override
  public Instruction withdrawStakeWithSlippage(final AccountInfo<StakePoolState> stakePoolStateAccountInfo,
                                               final PublicKey validatorOrReserveStakeAccount,
                                               final PublicKey uninitializedStakeAccount,
                                               final PublicKey stakeAccountWithdrawalAuthority,
                                               final PublicKey poolTokenATA,
                                               final long poolTokenAmount,
                                               final long lamportsOut) {
    final var stakePoolState = stakePoolStateAccountInfo.data();
    return StakePoolProgram.withdrawStakeWithSlippage(
        accounts,
        AccountMeta.createInvoked(stakePoolStateAccountInfo.owner()),
        stakePoolState.address(),
        stakePoolState.validatorList(),
        validatorOrReserveStakeAccount,
        uninitializedStakeAccount,
        stakeAccountWithdrawalAuthority,
        owner,
        poolTokenATA,
        stakePoolState.managerFeeAccount(),
        stakePoolState.poolMint(),
        stakePoolState.tokenProgramId(),
        poolTokenAmount,
        lamportsOut
    );
  }

  @Override
  public Instruction withdrawStakeWithSlippage(final AccountInfo<StakePoolState> stakePoolStateAccountInfo,
                                               final PublicKey validatorOrReserveStakeAccount,
                                               final PublicKey uninitializedStakeAccount,
                                               final PublicKey poolTokenATA,
                                               final long poolTokenAmount,
                                               final long lamportsOut) {
    return withdrawStakeWithSlippage(
        stakePoolStateAccountInfo,
        validatorOrReserveStakeAccount,
        uninitializedStakeAccount,
        owner,
        poolTokenATA,
        poolTokenAmount,
        lamportsOut
    );
  }


  @Override
  public Instruction withdrawStake(final AccountInfo<StakePoolState> stakePoolStateAccountInfo,
                                   final PublicKey validatorOrReserveStakeAccount,
                                   final PublicKey uninitializedStakeAccount,
                                   final PublicKey stakeAccountWithdrawalAuthority,
                                   final PublicKey poolTokenATA,
                                   final long poolTokenAmount) {
    final var stakePoolState = stakePoolStateAccountInfo.data();
    return StakePoolProgram.withdrawStake(
        accounts,
        AccountMeta.createInvoked(stakePoolStateAccountInfo.owner()),
        stakePoolState.address(),
        stakePoolState.validatorList(),
        validatorOrReserveStakeAccount,
        uninitializedStakeAccount,
        stakeAccountWithdrawalAuthority,
        owner,
        poolTokenATA,
        stakePoolState.managerFeeAccount(),
        stakePoolState.poolMint(),
        stakePoolState.tokenProgramId(),
        poolTokenAmount
    );
  }

  public Instruction withdrawStake(final AccountInfo<StakePoolState> stakePoolStateAccountInfo,
                                   final PublicKey validatorOrReserveStakeAccount,
                                   final PublicKey uninitializedStakeAccount,
                                   final PublicKey poolTokenATA,
                                   final long poolTokenAmount) {
    return withdrawStake(
        stakePoolStateAccountInfo,
        validatorOrReserveStakeAccount,
        uninitializedStakeAccount,
        owner,
        poolTokenATA,
        poolTokenAmount
    );
  }

  @Override
  public Instruction updateStakePoolBalance(final AccountInfo<StakePoolState> stakePoolStateAccountInfo) {
    final var stakePoolState = stakePoolStateAccountInfo.data();
    return StakePoolProgram.updateStakePoolBalance(
        AccountMeta.createInvoked(stakePoolStateAccountInfo.owner()),
        stakePoolState.address(),
        stakePoolState.validatorList(),
        stakePoolState.reserveStake(),
        stakePoolState.managerFeeAccount(),
        stakePoolState.poolMint(),
        stakePoolState.tokenProgramId()
    );
  }
}
