package software.sava.solana.programs.compute_budget;

import software.sava.core.accounts.meta.AccountMeta;
import software.sava.core.tx.Instruction;
import software.sava.core.encoding.ByteUtil;

import static software.sava.core.accounts.meta.AccountMeta.NO_KEYS;
import static software.sava.core.tx.Instruction.createInstruction;

public final class ComputeBudgetProgram {

  public static int COMPUTE_UNITS_CONSUMED = 300;

  public static Instruction setComputeUnitLimit(final AccountMeta invokedProgram, final int units) {
    final byte[] data = new byte[5];
    data[0] = (byte) 2;
    ByteUtil.putInt32LE(data, 1, units);
    return createInstruction(invokedProgram, NO_KEYS, data);
  }

  public static Instruction setComputeUnitPrice(final AccountMeta invokedProgram, final long microLamports) {
    final byte[] data = new byte[9];
    data[0] = (byte) 3;
    ByteUtil.putInt64LE(data, 1, microLamports);
    return createInstruction(invokedProgram, NO_KEYS, data);
  }

  private ComputeBudgetProgram() {
  }
}
