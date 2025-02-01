![](https://github.com/sava-software/sava/blob/003cf88b3cd2a05279027557f23f7698662d2999/assets/images/solana_java_cup.svg)

# Solana Programs [![Build](https://github.com/sava-software/anchor-src-gen/actions/workflows/gradle.yml/badge.svg)](https://github.com/sava-software/anchor-src-gen/actions/workflows/gradle.yml) [![Release](https://github.com/sava-software/anchor-src-gen/actions/workflows/release.yml/badge.svg)](https://github.com/sava-software/anchor-src-gen/actions/workflows/release.yml)

## Requirements

- The latest generally available JDK. This project will continue to move to the latest and will not maintain
  versions released against previous JDK's.

## [Dependencies](src/main/java/module-info.java)

- [JSON Iterator](https://github.com/comodal/json-iterator?tab=readme-ov-file#json-iterator)
- [Bouncy Castle](https://www.bouncycastle.org/download/bouncy-castle-java/#latest)
- [sava-core](https://github.com/sava-software/sava)
- [sava-rpc](https://github.com/sava-software/sava)

## Dependency Configuration

### GitHub Access Token

[Generate a classic token](https://github.com/settings/tokens) with the `read:packages` scope needed to access
dependencies hosted on GitHub Package Repository.

### Gradle

#### build.gradle

```groovy
repositories {
  maven {
    url = "https://maven.pkg.github.com/sava-software/sava"
    credentials {
      username = GITHUB_USERNAME
      password = GITHUB_PERSONAL_ACCESS_TOKEN
    }
  }
  maven {
    url = "https://maven.pkg.github.com/sava-software/solana-programs"
    credentials {
      username = GITHUB_USERNAME
      password = GITHUB_PERSONAL_ACCESS_TOKEN
    }
  }
}

dependencies {
  implementation "software.sava:sava-core:$VERSION"
  implementation "software.sava:sava-rpc:$VERSION"
  implementation "software.sava:solana-programs:$VERSION"
}
```

## Contribution

Unit tests are needed and welcomed. Otherwise, please open a discussion, issue or send an email before working on a pull
request.

## Durable Transactions

### Create & Initialize Nonce Account

```
final Signer signer = ...
 
try (final var httpClient = HttpClient.newHttpClient()) {
  final var rpcClient = SolanaRpcClient.createClient(SolanaNetwork.MAIN_NET.getEndpoint(), httpClient);

  final var blockHashFuture = rpcClient.getLatestBlockHash();
  final var minRentFuture = rpcClient.getMinimumBalanceForRentExemption(NonceAccount.BYTES);

  final var solanaAccounts = SolanaAccounts.MAIN_NET;
  final var nonceAccountWithSeed = PublicKey.createOffCurveAccountWithAsciiSeed(
      signer.publicKey(),
      "nonce",
      solanaAccounts.systemProgram()
  );

  final var initializeNonceAccountIx = SystemProgram.initializeNonceAccount(
      solanaAccounts,
      nonceAccountWithSeed.publicKey(),
      signer.publicKey()
  );
  
  System.out.format("""
          Fetching block hash and minimum rent to create nonce account %s with authority %s.
          
          """,
      nonceAccountWithSeed.publicKey(),
      signer.publicKey()
  );

  final long minRent = minRentFuture.join();
  final var createNonceAccountIx = SystemProgram.createAccountWithSeed(
      solanaAccounts.invokedSystemProgram(),
      signer.publicKey(),
      nonceAccountWithSeed,
      minRent,
      NonceAccount.BYTES,
      solanaAccounts.systemProgram()
  );

  final var instructions = List.of(createNonceAccountIx, initializeNonceAccountIx);
  final var transaction = Transaction.createTx(signer.publicKey(), instructions);

  final var blockHash = blockHashFuture.join().blockHash();
  transaction.setRecentBlockHash(blockHash);
  transaction.sign(signer);

  final var base64Encoded = transaction.base64EncodeToString();
  final var sendTransactionFuture = rpcClient.sendTransaction(base64Encoded);
  System.out.format("""
          Creating nonce account %s
          https://explorer.solana.com/tx/%s
          
          """,
      nonceAccountWithSeed.publicKey(),
      transaction.getBase58Id()
  );

  final var sig = sendTransactionFuture.join();
  System.out.format("""
          Confirmed transaction %s
          https://solscan.io/account/%s
          
          """,
      sig,
      nonceAccountWithSeed.publicKey()
  );

  final var nonceAccountInfo = rpcClient.getAccountInfo(nonceAccountWithSeed.publicKey()).join();
  final var nonceAccount = NonceAccount.read(nonceAccountInfo);
  System.out.println(nonceAccount);
}
```

### Create & Send Durable Transaction

```
final var signer = ...
final var nonceAccountKey = PublicKey.fromBase58Encoded("");
final var sendToKey = PublicKey.fromBase58Encoded("");
final var transferSOL = new BigDecimal("0.0");

final var solanaAccounts = SolanaAccounts.MAIN_NET;
try (final var httpClient = HttpClient.newHttpClient()) {
  final var rpcClient = SolanaRpcClient.createClient(SolanaNetwork.MAIN_NET.getEndpoint(), httpClient);

  final var nonceAccountInfo = rpcClient.getAccountInfo(nonceAccountKey).join();
  final var nonceAccount = NonceAccount.read(nonceAccountInfo);
  System.out.println(nonceAccount);

  final var advanceNonceIx = nonceAccount.advanceNonceAccount(solanaAccounts);
  final var transferIx = SystemProgram.transfer(
      solanaAccounts.invokedSystemProgram(),
      signer.publicKey(),
      sendToKey,
      LamportDecimal.fromBigDecimal(transferSOL).longValue()
  );

  final var instructions = List.of(advanceNonceIx, transferIx);
  final var transaction = Transaction.createTx(signer.publicKey(), instructions);
  transaction.setRecentBlockHash(nonceAccount.nonce());
  transaction.sign(signer);

  final var base64Encoded = transaction.base64EncodeToString();
  final var sendTransactionFuture = rpcClient.sendTransaction(base64Encoded);
  System.out.format("""
          Transferring %s SOL from %s to %s.
          https://explorer.solana.com/tx/%s
          
          """,
      transferSOL.toPlainString(), signer.publicKey(), sendToKey,
      transaction.getBase58Id()
  );

  final var sig = sendTransactionFuture.join();
  System.out.println("Confirmed transaction " + sig);
}
```
