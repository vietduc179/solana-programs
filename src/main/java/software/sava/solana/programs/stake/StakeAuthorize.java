package software.sava.solana.programs.stake;

import software.sava.core.accounts.PublicKey;
import software.sava.core.borsh.Borsh;
import software.sava.core.borsh.RustEnum;
import software.sava.core.encoding.ByteUtil;

import java.util.OptionalLong;

public sealed interface StakeAuthorize extends RustEnum permits StakeAuthorize.BlockHashQuery, StakeAuthorize.ComputeUnitPrice, StakeAuthorize.Custodian, StakeAuthorize.DumpTransactionMessage, StakeAuthorize.FeePayer, StakeAuthorize.Memo, StakeAuthorize.NewAuthorizations, StakeAuthorize.NoWait, StakeAuthorize.NonceAccount, StakeAuthorize.NonceAuthority, StakeAuthorize.SignOnly, StakeAuthorize.StakeAccountPubKey {

  static StakeAuthorize read(final byte[] data, final int offset) {
    final int ordinal = data[offset] & 0xFF;
    int i = offset + 1;
    return switch (ordinal) {
      case 0 -> StakeAccountPubKey.read(data, i);
      case 1 -> NewAuthorizations.read(data, i);
      case 2 -> SignOnly.read(data, i);
      case 3 -> DumpTransactionMessage.read(data, i);
      case 4 -> BlockHashQuery.read(data, i);
      case 5 -> NonceAccount.read(data, i);
      case 6 -> NonceAuthority.read(data, i);
      case 7 -> Memo.read(data, i);
      case 8 -> FeePayer.read(data, i);
      case 9 -> Custodian.read(data, i);
      case 10 -> NoWait.read(data, i);
      case 11 -> ComputeUnitPrice.read(data, i);
      default ->
          throw new IllegalStateException(java.lang.String.format("Unexpected ordinal [%d] for enum [BlockHashQuery].", ordinal));
    };
  }

  record StakeAccountPubKey(PublicKey val) implements StakeAuthorize, EnumPublicKey {

    public static StakeAccountPubKey read(final byte[] data, final int offset) {
      return new StakeAccountPubKey(PublicKey.readPubKey(data, offset));
    }

    @Override
    public int ordinal() {
      return 0;
    }
  }

  record NewAuthorizations(StakeAuthorizationIndexed[] val) implements StakeAuthorize, BorshVectorEnum {

    public static NewAuthorizations read(final byte[] data, final int offset) {
      return new NewAuthorizations(Borsh.readVector(StakeAuthorizationIndexed.class, StakeAuthorizationIndexed::read, data, offset));
    }

    @Override
    public int ordinal() {
      return 1;
    }
  }

  record SignOnly(boolean val) implements StakeAuthorize, EnumBool {

    public static SignOnly read(final byte[] data, final int offset) {
      return new SignOnly(data[offset] == 1);
    }

    @Override
    public int ordinal() {
      return 2;
    }
  }

  record DumpTransactionMessage(boolean val) implements StakeAuthorize, EnumBool {

    public static DumpTransactionMessage read(final byte[] data, final int offset) {
      return new DumpTransactionMessage(data[offset] == 1);
    }

    @Override
    public int ordinal() {
      return 3;
    }
  }

  record BlockHashQuery(boolean val) implements StakeAuthorize, EnumBool {

    public static BlockHashQuery read(final byte[] data, final int offset) {
      return new BlockHashQuery(data[offset] == 1);
    }

    @Override
    public int ordinal() {
      return 4;
    }
  }

  record NonceAccount(PublicKey val) implements StakeAuthorize, OptionalEnumPublicKey {

    public static StakeAccountPubKey read(final byte[] data, final int offset) {
      return new StakeAccountPubKey(data[offset] == 1 ? PublicKey.readPubKey(data, offset + 1) : null);
    }

    @Override
    public int ordinal() {
      return 5;
    }
  }

  record NonceAuthority(long val) implements StakeAuthorize, EnumInt64 {

    public static NonceAuthority read(final byte[] data, final int offset) {
      return new NonceAuthority(ByteUtil.getInt64LE(data, offset));
    }

    @Override
    public int ordinal() {
      return 6;
    }
  }

  record Memo(String _val, byte[] val) implements StakeAuthorize, OptionalEnumString {

    public static Memo read(final byte[] data, final int offset) {
      if (data[offset] == 1) {
        final var val = Borsh.read(data, offset + 1);
        return new Memo(new String(val), val);
      } else {
        return new Memo(null, new byte[0]);
      }
    }

    @Override
    public int ordinal() {
      return 7;
    }
  }

  record FeePayer(long val) implements StakeAuthorize, EnumInt64 {

    public static NonceAuthority read(final byte[] data, final int offset) {
      return new NonceAuthority(ByteUtil.getInt64LE(data, offset));
    }

    @Override
    public int ordinal() {
      return 8;
    }
  }

  record Custodian(OptionalLong val) implements StakeAuthorize, OptionalEnumInt64 {

    public static Custodian read(final byte[] data, final int offset) {
      return new Custodian(data[offset] == 1 ? OptionalLong.of(ByteUtil.getInt64LE(data, offset + 1)) : OptionalLong.empty());
    }

    @Override
    public int ordinal() {
      return 9;
    }
  }

  record NoWait(boolean val) implements StakeAuthorize, EnumBool {

    public static NoWait read(final byte[] data, final int offset) {
      return new NoWait(data[offset] == 1);
    }

    @Override
    public int ordinal() {
      return 10;
    }
  }

  record ComputeUnitPrice(OptionalLong val) implements StakeAuthorize, OptionalEnumInt64 {

    public static ComputeUnitPrice read(final byte[] data, final int offset) {
      return new ComputeUnitPrice(data[offset] == 1 ? OptionalLong.of(ByteUtil.getInt64LE(data, offset + 1)) : OptionalLong.empty());
    }

    @Override
    public int ordinal() {
      return 11;
    }
  }
}
