async function apiGet(path) {
  var response;
  try {
    response = await fetch(path);
  } catch (err) {
    throw new Error("Could not reach the server. Is the app running?");
  }
  var data = await response.json();
  if (!response.ok) {
    throw new Error(data.error || "Request failed");
  }
  return data;
}

async function apiPost(path, body) {
  var response;
  try {
    response = await fetch(path, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body || {})
    });
  } catch (err) {
    throw new Error("Could not reach the server. Is the app running?");
  }
  var data = await response.json();
  if (!response.ok && data.ok !== false) {
    throw new Error(data.error || "Request failed");
  }
  return data;
}

function showMessage(element, text, isError) {
  if (!element) {
    return;
  }
  element.textContent = text;
  element.hidden = !text;
  element.classList.toggle("alert-error", !!isError);
  element.classList.toggle("alert-ok", !isError && !!text);
}
