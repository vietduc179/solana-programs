package software.sava.solana.programs.token;

import software.sava.core.accounts.ProgramDerivedAddress;
import software.sava.core.accounts.PublicKey;
import software.sava.core.accounts.SolanaAccounts;
import software.sava.core.accounts.meta.AccountMeta;
import software.sava.core.tx.Instruction;

import java.util.List;

// https://github.com/solana-labs/solana-program-library/blob/d0f48a6ba34acb01dd0fde5368e73b406c544837/associated-token-account/program/src/instruction.rs#L15
public final class AssociatedTokenProgram {

  private static final byte[] CREATE = new byte[]{0};
  private static final byte[] CREATE_IDEMPOTENT = new byte[]{1};

  public static ProgramDerivedAddress findAssociatedTokenProgramAddress(final SolanaAccounts solanaAccounts,
                                                                        final PublicKey owner,
                                                                        final PublicKey mint) {
    return PublicKey.findProgramAddress(List.of(
        owner.toByteArray(),
        solanaAccounts.tokenProgram().toByteArray(),
        mint.toByteArray()
    ), solanaAccounts.associatedTokenAccountProgram());
  }

  public static Instruction createATA(final boolean idempotent,
                                      final SolanaAccounts solanaAccounts,
                                      final AccountMeta fundingAccount,
                                      final PublicKey programDerivedAddress,
                                      final PublicKey owner,
                                      final PublicKey mint) {
    final var keys = List.of(
        fundingAccount,
        AccountMeta.createWrite(programDerivedAddress),
        AccountMeta.createRead(owner),
        AccountMeta.createRead(mint),
        solanaAccounts.readSystemProgram(),
        solanaAccounts.readTokenProgram()
    );
    return Instruction.createInstruction(
        solanaAccounts.invokedAssociatedTokenAccountProgram(),
        keys,
        idempotent ? CREATE_IDEMPOTENT : CREATE
    );
  }

  public static Instruction createATA(final boolean idempotent,
                                      final SolanaAccounts solanaAccounts,
                                      final AccountMeta fundingAccount,
                                      final PublicKey owner,
                                      final PublicKey mint) {
    final var programDerivedAddress = findAssociatedTokenProgramAddress(solanaAccounts, owner, mint);
    return createATA(idempotent, solanaAccounts, fundingAccount, programDerivedAddress.publicKey(), owner, mint);
  }

  public static Instruction createATA(final boolean idempotent,
                                      final SolanaAccounts solanaAccounts,
                                      final AccountMeta fundingAccount,
                                      final PublicKey mint) {
    return createATA(idempotent, solanaAccounts, fundingAccount, fundingAccount.publicKey(), mint);
  }

  private AssociatedTokenProgram() {
  }
}
