const BASE_URL = 'http://localhost:8080';

document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('uploadForm');
  const fileInput = document.getElementById('fileInput');
  const documentList = document.getElementById('documentList');
  const detailsPanel = document.getElementById('documentDetails');
  const showOriginalBtn = document.getElementById('showOriginal');
  const originalPreview = document.getElementById('originalPreview');
  const searchInput = document.getElementById('searchInput');
  const searchButton = document.getElementById('searchButton');

  // Функция для поиска документов
  async function searchDocuments(query) {
    const res = await fetch(`${BASE_URL}/documents/search?text=${query}`);
    const data = await res.json();

    documentList.innerHTML = '';
    data.forEach(doc => {
      const li = document.createElement('li');
      li.textContent = `${doc.name} (${new Date(doc.updatedAt).toLocaleString()})`;
      li.addEventListener('click', () => showDetails(doc.id));
      documentList.appendChild(li);
    });
  }

  // Отображение деталей документа
  async function showDetails(id) {
    const res = await fetch(`${BASE_URL}/documents/details?id=${id}`);
    const doc = await res.json();

    detailsPanel.innerHTML = `
      <strong>Название:</strong> ${doc.name}<br>
      <strong>Категория:</strong> ${doc.category || '—'}<br>
      <strong>Создан:</strong> ${new Date(doc.createdAt).toLocaleString()}<br>
      <strong>Изменён:</strong> ${new Date(doc.lastModified).toLocaleString()}<br><br>
      <strong>Распознанный текст:</strong>
      <div style="border: 1px solid #ccc; padding: 5px; margin-top: 5px;">
        ${doc.hocrText}
      </div>
    `;

    showOriginalBtn.style.display = 'inline-block';
    showOriginalBtn.onclick = () => {
      const isImage = /\.(png|jpe?g|gif)$/i.test(doc.downloadUrl);
      const isPdf = /\.pdf$/i.test(doc.downloadUrl);

      if (isImage) {
        originalPreview.innerHTML = `<img src="${doc.downloadUrl}" style="max-width:100%;">`;
      } else if (isPdf) {
        originalPreview.innerHTML = `<iframe src="${doc.downloadUrl}" style="width:100%; height:500px;"></iframe>`;
      } else {
        originalPreview.innerHTML = `<a href="${doc.downloadUrl}" target="_blank">Скачать файл</a>`;
      }
    };
  }

  // Функция для загрузки документа
  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append('file', fileInput.files[0]);

    await fetch(`${BASE_URL}/documents/upload`, {
      method: 'POST',
      body: formData,
    });

    fileInput.value = '';
    fetchDocuments();
  });

  // Обработчик кнопки поиска
  searchButton.addEventListener('click', () => {
    const query = searchInput.value.trim();
    if (query) {
      searchDocuments(query);
    } else {
      fetchDocuments(); // Если поисковый запрос пустой, загружаем все документы
    }
  });

  // Функция для загрузки всех документов
  async function fetchDocuments() {
    const res = await fetch(`${BASE_URL}/documents/all`);
    const data = await res.json();

    documentList.innerHTML = '';
    data.forEach(doc => {
      const li = document.createElement('li');
      li.textContent = `${doc.name} (${new Date(doc.updatedAt).toLocaleString()})`;
      li.addEventListener('click', () => showDetails(doc.id));
      documentList.appendChild(li);
    });
  }

  fetchDocuments(); // Изначальная загрузка всех документов
});
