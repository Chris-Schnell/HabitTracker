const habitsContainer = document.getElementById('habits-container');
const newHabitInput = document.getElementById('new-habit');
const addHabitBtn = document.getElementById('add-habit');
const daysToShow = 30;
const today = new Date();

let currentView = 'month';
let currentYear = new Date().getFullYear();
let currentMonth = new Date().getMonth(); // 0-based

const monthPicker = document.getElementById('monthPicker');
monthPicker.value = `${currentYear}-${String(currentMonth + 1).padStart(2, '0')}`;

monthPicker.addEventListener('change', (e) => {
    const [year, month] = e.target.value.split('-').map(Number);
    currentYear = year;
    currentMonth = month - 1;
    loadHabits();
});

// Load all habits from backend
async function loadHabits() {
    const res = await fetch('/habits');
    const habitList = await res.json();
    habitsContainer.innerHTML = '';
    for (let habit of habitList) {
        renderHabit(habit.name);
    }
}

document.getElementById('monthViewBtn').addEventListener('click', () => {
    currentView = 'month';
    document.getElementById('monthViewBtn').classList.add('active');
    document.getElementById('yearViewBtn').classList.remove('active');
    loadHabits();
});

document.getElementById('yearViewBtn').addEventListener('click', () => {
    currentView = 'year';
    document.getElementById('yearViewBtn').classList.add('active');
    document.getElementById('monthViewBtn').classList.remove('active');
    loadHabits();
});

// Add new habit
addHabitBtn.addEventListener('click', async () => {
    const name = newHabitInput.value.trim();
    if (!name) return;
    await fetch(`/habits/add?name=${encodeURIComponent(name)}`, { method: 'POST' });
    newHabitInput.value = '';
    loadHabits();
});

function setView(view) {
    currentView = view;
    document.querySelectorAll('.grid').forEach(g => {
        g.classList.remove('month', 'year');
        g.classList.add(view);
    });
    loadHabits();
}

// Render one habit grid (same as before)
async function renderHabit(habitName) {
    const container = document.createElement('div');
    container.classList.add('habit');

    const title = document.createElement('h2');
    title.textContent = habitName;
    container.appendChild(title);

    const removeBtn = document.createElement('button');
    removeBtn.innerHTML = `<i class="fa-solid fa-trash"></i>`;
    removeBtn.style.color = "#e53935";
    removeBtn.style.marginLeft = "10px";
    removeBtn.style.cursor = "pointer";
    removeBtn.style.background = "none";
    removeBtn.style.border = "none";


    removeBtn.addEventListener('click', async () => {
        if (confirm(`Are you sure you want to remove "${habitName}"?`)) {
            await fetch(`/habits/delete/${encodeURIComponent(habitName)}`, { method: 'DELETE' });
            loadHabits(); // refresh the habit list
        }
    });

    title.appendChild(removeBtn); // append the button to the title

    const grid = document.createElement('div');
    grid.classList.add('grid');

    grid.classList.add('grid', currentView);
    container.appendChild(grid);



    const res = await fetch(`/habits/entry/${habitName}`);
    const data = await res.json();
    const completedDates = new Set(data.filter(e => e.completed).map(e => e.date));

    let daysToRender = [];
    if (currentView === 'month') {
        const daysInMonth = new Date(currentYear, currentMonth + 1, 0).getDate();
        for (let day = 1; day <= daysInMonth; day++) {
            daysToRender.push(new Date(currentYear, currentMonth, day));
        }
    } else if (currentView === 'year') {
        const daysInYear = (new Date(currentYear, 1, 29).getMonth() === 1) ? 366 : 365; // leap year check
        for (let day = 1; day <= daysInYear; day++) {
            const date = new Date(currentYear, 0, day);
            daysToRender.push(date);
        }
    }

    for (const date of daysToRender) {
        const iso = date.toISOString().split('T')[0];

        const cell = document.createElement('div');
        cell.classList.add('cell');
        cell.dataset.date = iso;
        if (completedDates.has(iso)) cell.classList.add('done');

        cell.addEventListener('click', async () => {
            await fetch(`/habits/entry/${habitName}/${iso}/toggle`, { method: 'POST' });
            cell.classList.toggle('done');
        });

        grid.appendChild(cell);
    }

    habitsContainer.appendChild(container);
}

// Initial load
loadHabits();
