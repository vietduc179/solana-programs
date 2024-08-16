package software.sava.solana.programs.stake;

import software.sava.core.borsh.RustEnum;

import java.util.Arrays;

public sealed interface BlockHashQuery extends RustEnum
    permits BlockHashQuery.All, BlockHashQuery.FeeCalculator, BlockHashQuery.None {

  static BlockHashQuery read(final byte[] _data, final int offset) {
    final int ordinal = _data[offset] & 0xFF;
    int i = offset + 1;
    return switch (ordinal) {
      case 0 -> None.read(_data, i);
      case 1 -> FeeCalculator.read(_data, i);
      case 2 -> All.read(_data, i);
      default -> throw new IllegalStateException(String.format(
          "Unexpected ordinal [%d] for enum [BlockHashQuery].",
          ordinal
      ));
    };
  }

  record None(byte[] val) implements BlockHashQuery, EnumBytes {

    public static None read(final byte[] _data, final int offset) {
      return new None(Arrays.copyOfRange(_data, offset, offset + 32));
    }

    @Override
    public int ordinal() {
      return 0;
    }
  }

  record FeeCalculator(Source source, byte[] hash) implements BlockHashQuery, RustEnum {

    public static None read(final byte[] _data, final int offset) {
      final var source = Source.read(_data, offset);
      final int i = offset + source.l();
      return new None(Arrays.copyOfRange(_data, i, i + 32));
    }

    @Override
    public int ordinal() {
      return 1;
    }

    @Override
    public int l() {
      return 1 + source.l() + hash.length;
    }

    @Override
    public int write(final byte[] data, final int offset) {
      int i = writeOrdinal(data, offset);
      i += source.write(data, i);
      System.arraycopy(hash, 0, data, i, hash.length);
      i += hash.length;
      return i - offset;
    }
  }

  record All(Source val) implements BlockHashQuery, BorshEnum {

    public static All read(final byte[] _data, final int offset) {
      return new All(Source.read(_data, offset));
    }

    @Override
    public int ordinal() {
      return 2;
    }
  }
}
