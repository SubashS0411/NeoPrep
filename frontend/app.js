const baseUrl = 'http://localhost:8080';
const defaultHeaders = {
  'X-API-KEY': 'local-dev-key',
  'Content-Type': 'application/json'
};

const output = document.getElementById('output');
const optOutput = document.getElementById('optOutput');
const standupOutput = document.getElementById('standupOutput');
const patternOutput = document.getElementById('patternOutput');

let latestRoadmapId = null;
let latestUserId = null;

document.getElementById('generate').addEventListener('click', async () => {
  const payload = {
    email: document.getElementById('email').value,
    targetCompanyTier: document.getElementById('tier').value,
    jobDescription: document.getElementById('jd').value
  };

  const resp = await fetch(`${baseUrl}/api/onboarding/roadmap`, {
    method: 'POST',
    headers: defaultHeaders,
    body: JSON.stringify(payload)
  });

  const json = await resp.json();
  latestRoadmapId = json.roadmapId;
  const firstDay = json.days?.[0];
  output.textContent = JSON.stringify({ roadmapId: json.roadmapId, firstDay }, null, 2);

  const userResp = await fetch(`${baseUrl}/api/progress/1`, { headers: { 'X-API-KEY': 'local-dev-key' } });
  if (userResp.ok) latestUserId = 1;
});

document.getElementById('recalculate').addEventListener('click', async () => {
  if (!latestRoadmapId) {
    output.textContent = 'Generate roadmap first.';
    return;
  }

  const userId = latestUserId || Number(document.getElementById('standupUserId').value || 1);
  const resp = await fetch(`${baseUrl}/api/roadmaps/${latestRoadmapId}/recalculate`, {
    method: 'POST',
    headers: defaultHeaders,
    body: JSON.stringify({
      userId,
      missedDays: [2, 3],
      weakTopics: ['Spring Boot Security']
    })
  });

  output.textContent = JSON.stringify(await resp.json(), null, 2);
});

document.getElementById('optimize').addEventListener('click', async () => {
  const formData = new FormData();
  formData.append('userId', document.getElementById('optUserId').value || '1');
  formData.append('problemName', document.getElementById('problem').value);
  formData.append('codeText', document.getElementById('codeText').value);

  const resp = await fetch(`${baseUrl}/api/submissions/optimize`, {
    method: 'POST',
    headers: { 'X-API-KEY': 'local-dev-key' },
    body: formData
  });

  optOutput.textContent = JSON.stringify(await resp.json(), null, 2);
});

document.getElementById('standup').addEventListener('click', async () => {
  const userId = document.getElementById('standupUserId').value || '1';
  const resp = await fetch(`${baseUrl}/api/standups/${userId}/generate`, {
    method: 'POST',
    headers: { 'X-API-KEY': 'local-dev-key' }
  });

  standupOutput.textContent = JSON.stringify(await resp.json(), null, 2);
});

document.getElementById('readiness').addEventListener('click', async () => {
  const userId = document.getElementById('standupUserId').value || '1';
  const company = document.getElementById('company').value;
  const resp = await fetch(`${baseUrl}/api/advanced/readiness/${userId}?company=${encodeURIComponent(company)}`, {
    headers: { 'X-API-KEY': 'local-dev-key' }
  });

  standupOutput.textContent = JSON.stringify(await resp.json(), null, 2);
});

document.getElementById('generatePattern').addEventListener('click', async () => {
  const payload = {
    company: document.getElementById('vaultCompany').value,
    tier: document.getElementById('vaultTier').value,
    styleHint: document.getElementById('vaultStyle').value
  };

  const resp = await fetch(`${baseUrl}/api/pattern-vault/generate`, {
    method: 'POST',
    headers: defaultHeaders,
    body: JSON.stringify(payload)
  });

  patternOutput.textContent = JSON.stringify(await resp.json(), null, 2);
});
