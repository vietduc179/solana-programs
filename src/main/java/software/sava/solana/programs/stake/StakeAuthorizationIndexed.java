package software.sava.solana.programs.stake;

import software.sava.core.accounts.PublicKey;
import software.sava.core.borsh.Borsh;
import software.sava.core.encoding.ByteUtil;

public record StakeAuthorizationIndexed(StakeAuthorize stakeAuthorize,
                                        PublicKey newAuthorityPublicKey,
                                        int authorityIndex,
                                        int newAuthorityIndex) implements Borsh {


  public static StakeAuthorizationIndexed read(final byte[] data, final int offset) {
    final var stakeAuthorize = StakeAuthorize.read(data, offset);
    int i = offset + stakeAuthorize.l();
    final var newAuthorityPublicKey = PublicKey.readPubKey(data, i);
    i += PublicKey.PUBLIC_KEY_LENGTH;
    final int authorityIndex = (int) ByteUtil.getInt64LE(data, i);
    i += Long.BYTES;
    final int newAuthorityIndex = (int) ByteUtil.getInt64LE(data, i);
    return new StakeAuthorizationIndexed(stakeAuthorize, newAuthorityPublicKey, authorityIndex, newAuthorityIndex);
  }

  @Override
  public int l() {
    return stakeAuthorize.l() + PublicKey.PUBLIC_KEY_LENGTH + Long.BYTES + Long.BYTES;
  }

  @Override
  public int write(final byte[] data, final int offset) {
    int i = stakeAuthorize.write(data, offset);
    i += newAuthorityPublicKey.write(data, i);
    ByteUtil.putInt64LE(data, i, authorityIndex);
    i += Long.BYTES;
    ByteUtil.putInt64LE(data, i, newAuthorityIndex);
    i += Long.BYTES;
    return i - offset;
  }
}
