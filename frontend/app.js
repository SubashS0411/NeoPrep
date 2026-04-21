const output = document.getElementById('output');

document.getElementById('generate').addEventListener('click', async () => {
  const payload = {
    email: document.getElementById('email').value,
    targetCompanyTier: document.getElementById('tier').value,
    jobDescription: document.getElementById('jd').value
  };

  const resp = await fetch('http://localhost:8080/api/onboarding/roadmap', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'X-API-KEY': 'local-dev-key'
    },
    body: JSON.stringify(payload)
  });

  const json = await resp.json();
  output.textContent = JSON.stringify(json.days?.slice(0, 3), null, 2);
});
