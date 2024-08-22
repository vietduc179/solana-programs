package software.sava.solana.programs.stake;

import software.sava.core.borsh.Borsh;
import software.sava.core.encoding.ByteUtil;

public enum StakeAuthorize implements Borsh {

  Staker,
  Withdrawer;

  @Override
  public int l() {
    return Integer.BYTES;
  }

  @Override
  public int write(final byte[] data, final int offset) {
    ByteUtil.putInt32LE(data, offset, ordinal());
    return l();
  }

  public static StakeAuthorize read(final byte[] _data, final int offset) {
    return Borsh.read(StakeAuthorize.values(), _data, offset);
  }
}
