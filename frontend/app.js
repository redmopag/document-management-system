const api = 'http://localhost:8080/documents';

const eventSource = new EventSource(`${api}/status/stream`);

eventSource.addEventListener('status-update', (event) => {
    const data = JSON.parse(event.data);
    const { documentId, status } = data;

    // Найдём строку таблицы с соответствующим documentId
    const row = document.querySelector(`#documentsTable tbody tr[data-id='${documentId}']`);
    if (row) {
        // Обновим ячейку со статусом
        const statusCell = row.querySelector('.status-cell');
        if (statusCell) {
            statusCell.textContent = status;
        }
    }
});

eventSource.onerror = (error) => {
    console.error('Ошибка SSE:', error);
};


function showSection(id) {
    document.querySelectorAll('.section').forEach(s => s.classList.add('hidden'));
    document.getElementById(id).classList.remove('hidden');
}

function switchTab(tabName) {
    document.querySelectorAll('.tab-content').forEach(el => el.classList.add('hidden'));
    document.getElementById(`tab-${tabName}`).classList.remove('hidden');
}

// Переключение между изображениями и PDF
document.getElementById('docType').addEventListener('change', function () {
    const type = this.value;
    document.getElementById('imageFiles').parentElement.classList.toggle('hidden', type !== 'image');
    document.getElementById('pdfInput').classList.toggle('hidden', type !== 'pdf');
});

// Загрузка документа
document.getElementById('uploadForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const data = new FormData();

    const docType = document.getElementById('docType').value;
    const username = document.getElementById('uploadUsername').value;
    const category = document.querySelector('input[name="category"]').value;
    const expirationDate = document.querySelector('input[name="expirationDate"]').value;

    // Добавляем нужные файлы вручную
    if (docType === 'image') {
        const images = document.getElementById('imageFiles').files;
        for (const img of images) {
            data.append('files', img);
        }
    } else if (docType === 'pdf') {
        const pdf = document.querySelector('#pdfInput input[type="file"]').files[0];
        if (pdf) {
            data.append('files', pdf);
        }
    }

    // Остальные поля
    if (category) data.append('category', category);
    if (expirationDate) data.append('expirationDate', expirationDate);
    data.append('username', username);

    const res = await fetch(`${api}/upload`, {
        method: 'POST',
        body: data
    });

    if (!res.ok) {
        const errorText = await res.text();
        console.error("Ошибка:", errorText);
        alert("Ошибка при загрузке файла.");
        return;
    }

    e.target.reset();
    showSection('list');
    loadAllDocuments();
});

// Загрузка всех документов
async function loadAllDocuments() {
    const res = await fetch(`${api}/all`);
    const docs = await res.json();
    const tbody = document.querySelector('#documentsTable tbody');
    tbody.innerHTML = '';
    docs.forEach(d => {
        const tr = document.createElement('tr');
        tr.setAttribute('data-id', d.id); // Добавляем data-id для идентификации строки
        tr.innerHTML = `
            <td>${d.id}</td>
            <td>${d.userName}</td>
            <td>${d.name}</td>
            <td class="status-cell">${d.status}</td> <!-- Добавляем класс для ячейки статуса -->
            <td>${new Date(d.updatedAt).toLocaleString()}</td>
            <td>
                <button onclick="loadDetails(${d.id})">Подробнее</button>
                <button onclick="editDocument(${d.id})">✏</button>
                <button onclick="deleteDocument(${d.id})">🗑</button>
            </td>`;
        tbody.appendChild(tr);
    });
    showSection('list');
}


// Поиск
async function searchDocuments() {
    const text = document.getElementById('searchInput').value;
    const username = document.getElementById('searchUsername').value;
    if (!text) return;
    const res = await fetch(`${api}/search?text=${encodeURIComponent(text)}&username=${encodeURIComponent(username)}`);
    const docs = await res.json();
    const container = document.getElementById('searchResults');
    container.innerHTML = docs.map(d =>
        `<p>${d.name} — <button onclick="loadDetails(${d.id})">Открыть</button></p>`
    ).join('');
    showSection('searchResultsSection');
}

// Подробности
async function loadDetails(id) {
    const res = await fetch(`${api}/details?id=${id}`);
    const d = await res.json();

    const html = `
        <p><strong>Название:</strong> ${d.name}</p>
        <p><strong>Категория:</strong> ${d.category}</p>
        <p><strong>Дата загрузки:</strong> ${d.uploadedAt}</p>
        <p><strong>Дата обновления:</strong> ${d.updatedAt}</p>
        <p><strong>Дата истечения:</strong> ${d.expirationDate || '-'}</p>

        <div class="tab-container">
            <div class="tabs">
                <button onclick="switchTab('original')">🖼️ Оригинал</button>
                <button onclick="switchTab('text')">🔍 Распознанный текст</button>
            </div>
            <div id="tab-original" class="tab-content">
                <object class="pdf" 
                    data="${d.downloadUrl}" 
                    style="width: 100%; 
                    height: 500px; 
                    border: 0px"></object>
            </div>
            <div id="tab-text" class="tab-content hidden">
                <iframe style="width: 100%; height: 500px; border: 1px solid #ccc; margin-top: 10px;"
                        srcdoc='${d.text}'></iframe>
            </div>
        </div>
    `;

    document.getElementById('documentDetails').innerHTML = html;
    showSection('details');
}

// Удаление
async function deleteDocument(id) {
    if (!confirm('Удалить документ?')) return;
    await fetch(`${api}/delete?id=${id}`, { method: 'DELETE' });
    loadAllDocuments();
}

// Редактирование
async function editDocument(id) {
    const res = await fetch(`${api}/details?id=${id}`);
    const d = await res.json();
    const form = document.getElementById('editForm');
    form.id.value = d.id;
    form.name.value = d.name;
    form.category.value = d.category;
    form.expirationDate.value = d.expirationDate || '';
    showSection('edit');
}

document.getElementById('editForm').addEventListener('submit', async function (e) {
    e.preventDefault();
    const form = e.target;
    const body = {
        id: form.id.value,
        name: form.name.value,
        category: form.category.value,
        expirationDate: form.expirationDate.value || null
    };
    await fetch(`${api}/update`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
    });
    loadAllDocuments();
});
