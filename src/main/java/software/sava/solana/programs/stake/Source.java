package software.sava.solana.programs.stake;

import software.sava.core.accounts.PublicKey;
import software.sava.core.borsh.RustEnum;

import static software.sava.core.accounts.PublicKey.readPubKey;

public sealed interface Source extends RustEnum permits Source.Cluster, Source.NonceAccount {

  static Source read(final byte[] _data, final int offset) {
    final int ordinal = _data[offset] & 0xFF;
    int i = offset + 1;
    return switch (ordinal) {
      case 0 -> Cluster.INSTANCE;
      case 1 -> NonceAccount.read(_data, i);
      default ->
          throw new IllegalStateException(java.lang.String.format("Unexpected ordinal [%d] for enum [Source].", ordinal));
    };
  }

  record Cluster() implements Source, EnumNone {

    private static final Cluster INSTANCE = new Cluster();

    @Override
    public int ordinal() {
      return 0;
    }
  }

  record NonceAccount(PublicKey val) implements Source, EnumPublicKey {

    public static NonceAccount read(final byte[] _data, final int offset) {
      return new NonceAccount(readPubKey(_data, offset));
    }

    @Override
    public int ordinal() {
      return 1;
    }
  }
}
