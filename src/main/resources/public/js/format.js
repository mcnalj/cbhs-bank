function money(value) {
  var n = Number(value);
  if (Number.isNaN(n)) {
    return "$0.00";
  }
  return "$" + n.toFixed(2);
}

function typeLabel(type) {
  if (type === "checking") {
    return "Checking";
  }
  if (type === "savings") {
    return "Savings";
  }
  return type;
}

function txnLabel(type) {
  if (type === "deposit") {
    return "Deposit";
  }
  if (type === "withdraw") {
    return "Withdraw";
  }
  if (type === "transfer-in") {
    return "Transfer in";
  }
  if (type === "transfer-out") {
    return "Transfer out";
  }
  if (type === "interest") {
    return "Interest";
  }
  return type;
}
