package software.sava.solana.programs.token;

import software.sava.core.accounts.ProgramDerivedAddress;
import software.sava.core.accounts.PublicKey;
import software.sava.core.accounts.SolanaAccounts;
import software.sava.core.accounts.meta.AccountMeta;
import software.sava.core.tx.Instruction;

import java.util.List;

import static software.sava.core.accounts.meta.AccountMeta.*;

public final class AssociatedTokenProgram {

  private enum Instructions {

    // https://github.com/solana-labs/solana-program-library/blob/d0f48a6ba34acb01dd0fde5368e73b406c544837/associated-token-account/program/src/instruction.rs#L15

    /// Creates an associated token account for the given wallet address and
    /// token mint Returns an error if the account exists.
    ///
    ///   0. `[writeable,signer]` Funding account (must be a system account)
    ///   1. `[writeable]` Associated token account address to be created
    ///   2. `[]` Wallet address for the new associated token account
    ///   3. `[]` The token mint for the new associated token account
    ///   4. `[]` System program
    ///   5. `[]` SPL Token program
    Create,
    /// Creates an associated token account for the given wallet address and
    /// token mint, if it doesn't already exist.  Returns an error if the
    /// account exists, but with a different owner.
    ///
    ///   0. `[writeable,signer]` Funding account (must be a system account)
    ///   1. `[writeable]` Associated token account address to be created
    ///   2. `[]` Wallet address for the new associated token account
    ///   3. `[]` The token mint for the new associated token account
    ///   4. `[]` System program
    ///   5. `[]` SPL Token program
    CreateIdempotent,
    /// Transfers from and closes a nested associated token account: an
    /// associated token account owned by an associated token account.
    ///
    /// The tokens are moved from the nested associated token account to the
    /// wallet's associated token account, and the nested account lamports are
    /// moved to the wallet.
    ///
    /// Note: Nested token accounts are an anti-pattern, and almost always
    /// created unintentionally, so this instruction should only be used to
    /// recover from errors.
    ///
    ///   0. `[writeable]` Nested associated token account, must be owned by `3`
    ///   1. `[]` Token mint for the nested associated token account
    ///   2. `[writeable]` Wallet's associated token account
    ///   3. `[]` Owner associated token account address, must be owned by `5`
    ///   4. `[]` Token mint for the owner associated token account
    ///   5. `[writeable, signer]` Wallet address for the owner associated token
    ///      account
    ///   6. `[]` SPL Token program
    RecoverNested;

    private final byte[] discriminatorBytes;

    Instructions() {
      this.discriminatorBytes = new byte[]{(byte) this.ordinal()};
    }
  }

  public static ProgramDerivedAddress findATA(final SolanaAccounts solanaAccounts,
                                              final PublicKey tokenProgram,
                                              final PublicKey owner,
                                              final PublicKey mint) {
    return PublicKey.findProgramAddress(List.of(
        owner.toByteArray(),
        tokenProgram.toByteArray(),
        mint.toByteArray()
    ), solanaAccounts.associatedTokenAccountProgram());
  }

  public static ProgramDerivedAddress findATA(final SolanaAccounts solanaAccounts,
                                              final PublicKey owner,
                                              final PublicKey mint) {
    return findATA(solanaAccounts, solanaAccounts.tokenProgram(), owner, mint);
  }

  public static ProgramDerivedAddress findATA2022(final SolanaAccounts solanaAccounts,
                                                  final PublicKey owner,
                                                  final PublicKey mint) {
    return findATA(solanaAccounts, solanaAccounts.token2022Program(), owner, mint);
  }

  public static Instruction createATAForProgram(final boolean idempotent,
                                                final SolanaAccounts solanaAccounts,
                                                final AccountMeta tokenProgram,
                                                final PublicKey fundingAccount,
                                                final PublicKey programDerivedAddress,
                                                final PublicKey owner,
                                                final PublicKey mint) {
    final var keys = List.of(
        createWritableSigner(fundingAccount),
        createWrite(programDerivedAddress),
        createRead(owner),
        createRead(mint),
        solanaAccounts.readSystemProgram(),
        tokenProgram
    );
    return Instruction.createInstruction(
        solanaAccounts.invokedAssociatedTokenAccountProgram(),
        keys,
        idempotent
            ? Instructions.CreateIdempotent.discriminatorBytes
            : Instructions.Create.discriminatorBytes
    );
  }

  public static Instruction createATAForProgram(final boolean idempotent,
                                                final SolanaAccounts solanaAccounts,
                                                final PublicKey tokenProgram,
                                                final PublicKey fundingAccount,
                                                final PublicKey programDerivedAddress,
                                                final PublicKey owner,
                                                final PublicKey mint) {
    return createATAForProgram(
        idempotent,
        solanaAccounts,
        createRead(tokenProgram),
        fundingAccount,
        programDerivedAddress,
        owner,
        mint
    );
  }

  public static Instruction createATAForProgram(final boolean idempotent,
                                                final SolanaAccounts solanaAccounts,
                                                final AccountMeta tokenProgram,
                                                final PublicKey fundingAccount,
                                                final PublicKey owner,
                                                final PublicKey mint) {
    final var pda = findATA(solanaAccounts, tokenProgram.publicKey(), owner, mint);
    return createATAForProgram(idempotent, solanaAccounts, tokenProgram, fundingAccount, pda.publicKey(), owner, mint);
  }

  public static Instruction createATAForProgram(final boolean idempotent,
                                                final SolanaAccounts solanaAccounts,
                                                final PublicKey tokenProgram,
                                                final PublicKey fundingAccount,
                                                final PublicKey owner,
                                                final PublicKey mint) {
    return createATAForProgram(idempotent, solanaAccounts, createRead(tokenProgram), fundingAccount, owner, mint);
  }

  public static Instruction createATA(final boolean idempotent,
                                      final SolanaAccounts solanaAccounts,
                                      final PublicKey fundingAccount,
                                      final PublicKey programDerivedAddress,
                                      final PublicKey owner,
                                      final PublicKey mint) {
    return createATAForProgram(
        idempotent,
        solanaAccounts,
        solanaAccounts.readTokenProgram(),
        fundingAccount,
        programDerivedAddress,
        owner,
        mint
    );
  }

  public static Instruction createATA(final boolean idempotent,
                                      final SolanaAccounts solanaAccounts,
                                      final PublicKey fundingAccount,
                                      final PublicKey owner,
                                      final PublicKey mint) {
    return createATAForProgram(
        idempotent,
        solanaAccounts,
        solanaAccounts.readTokenProgram(),
        fundingAccount,
        owner,
        mint
    );
  }

  public static Instruction createATA(final boolean idempotent,
                                      final SolanaAccounts solanaAccounts,
                                      final PublicKey fundingAccount,
                                      final PublicKey mint) {
    return createATA(idempotent, solanaAccounts, fundingAccount, fundingAccount, mint);
  }

  public static Instruction createATA2022(final boolean idempotent,
                                           final SolanaAccounts solanaAccounts,
                                           final PublicKey fundingAccount,
                                           final PublicKey programDerivedAddress,
                                           final PublicKey owner,
                                           final PublicKey mint) {
    return createATAForProgram(
        idempotent,
        solanaAccounts,
        solanaAccounts.readToken2022Program(),
        fundingAccount,
        programDerivedAddress,
        owner,
        mint
    );
  }

  public static Instruction createATA2022(final boolean idempotent,
                                          final SolanaAccounts solanaAccounts,
                                          final PublicKey fundingAccount,
                                          final PublicKey owner,
                                          final PublicKey mint) {
    return createATAForProgram(
        idempotent,
        solanaAccounts,
        solanaAccounts.readToken2022Program(),
        fundingAccount,
        owner,
        mint
    );
  }

  public static Instruction createATA2022(final boolean idempotent,
                                          final SolanaAccounts solanaAccounts,
                                          final PublicKey fundingAccount,
                                          final PublicKey mint) {
    return createATA2022(idempotent, solanaAccounts, fundingAccount, fundingAccount, mint);
  }

  private AssociatedTokenProgram() {
  }
}
