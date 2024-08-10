module software.sava.solana_programs {
  requires java.net.http;

  requires systems.comodal.json_iterator;

  requires software.sava.core;
  requires software.sava.rpc;

  exports software.sava.solana.programs.clients;
  exports software.sava.solana.programs.stake;
  exports software.sava.solana.programs.stakepool;
  exports software.sava.solana.programs.token;
  exports software.sava.solana.programs.compute_budget;
  exports software.sava.solana.programs.address_lookup_table;
}
