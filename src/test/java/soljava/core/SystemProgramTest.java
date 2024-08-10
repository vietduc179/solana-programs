package software.sava.core;

import org.junit.jupiter.api.Test;
import software.sava.core.accounts.meta.AccountMeta;
import software.sava.core.accounts.PublicKey;
import software.sava.core.accounts.SolanaAccounts;
import software.sava.core.encoding.Base58;
import software.sava.solana.programs.system.SystemProgram;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static software.sava.core.accounts.SolanaAccounts.MAIN_NET;

final class SystemProgramTest {

  @Test
  public void transferInstruction() {
    final var fromPublicKey = PublicKey.fromBase58Encoded("QqCCvshxtqMAL2CVALqiJB7uEeE5mjSPsseQdDzsRUo");
    final var toPublicKey = PublicKey.fromBase58Encoded("GrDMoeqMLFjeXQ24H56S1RLgT4R76jsuWCd6SvXyGPQ5");
    final int lamports = 3000;

    final var instruction = SystemProgram.transfer(
        SolanaAccounts.MAIN_NET.invokedSystemProgram(),
        AccountMeta.createFeePayer(fromPublicKey),
        toPublicKey,
        lamports
    );

    assertEquals(MAIN_NET.invokedSystemProgram(), instruction.programId());
    assertEquals(2, instruction.accounts().size());
    assertEquals(toPublicKey, instruction.accounts().get(1).publicKey());

    assertArrayEquals(new byte[]{2, 0, 0, 0, -72, 11, 0, 0, 0, 0, 0, 0}, instruction.data());
  }

  @Test
  public void createAccountInstruction() {
    final var instruction = SystemProgram.createAccount(
        MAIN_NET.invokedSystemProgram(),
        AccountMeta.createFeePayer(MAIN_NET.systemProgram()),
        MAIN_NET.systemProgram(), 2039280, 165,
        MAIN_NET.systemProgram()
    );

    assertEquals("11119os1e9qSs2u7TsThXqkBSRUo9x7kpbdqtNNbTeaxHGPdWbvoHsks9hpp6mb2ed1NeB",
        Base58.encode(instruction.data()));
  }
}
