// export-pdf.js - Dedicated logic for generating the Timesheet PDF

function exportToPDF() {
    // 1. Safety check to ensure the PDF library loaded from the HTML file
    if (!window.jspdf) {
        alert("PDF Library has not loaded yet. Please try again in a second.");
        return;
    }

    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    // 2. Grab Employee Details
    const empNameEl = document.getElementById("empName");
    const empIdEl = document.getElementById("empId");
    const weekInputEl = document.getElementById("weekInput");

    const empName = empNameEl && empNameEl.value ? empNameEl.value : "Employee";
    const empId = empIdEl && empIdEl.value ? empIdEl.value : "N/A";
    const weekVal = weekInputEl && weekInputEl.value ? weekInputEl.value : "N/A";

    // 3. Add Header to PDF
    doc.setFontSize(18);
    doc.setTextColor(30, 58, 138);
    doc.text("Weekly Timesheet Report", 14, 20);

    doc.setFontSize(11);
    doc.setTextColor(80, 80, 80);
    doc.text(`Employee Name: ${empName}`, 14, 30);
    doc.text(`Employee ID: ${empId}`, 14, 36);
    doc.text(`Selected Week: ${weekVal}`, 14, 42);

    // 4. Extract Table Data
    const tableData = [];
    for (let i = 0; i < 7; i++) {
        const row = document.getElementById(`row-${i}`);
        if (row) {
            const dayDate = row.cells[0].innerText.trim();
            const plannedHrs = row.cells[1].innerText.trim();

            // Get Recorded Hours
            const recInput = row.querySelector('input[type="number"]');
            const recordedHrs = recInput ? recInput.value : "0.0";

            // Get Status
            const statusSelect = row.querySelector('.status-select');
            const readonlyStatus = row.cells[3].querySelector('.readonly-cell');
            let status = "-";
            if (statusSelect) status = statusSelect.options[statusSelect.selectedIndex].text;
            else if (readonlyStatus) status = readonlyStatus.value;

            // Get Mode/Reason
            const modeSelect = row.querySelector('.mode-select');
            const reasonInput = row.querySelector('.reason-input');
            const readonlyMode = row.cells[4].querySelector('.readonly-cell');
            let modeReason = "-";

            if (modeSelect) {
                modeReason = modeSelect.options[modeSelect.selectedIndex].text;
                if (reasonInput && !reasonInput.classList.contains('d-none') && reasonInput.value.trim() !== '') {
                    modeReason += ` (${reasonInput.value.trim()})`;
                }
            } else if (readonlyMode) {
                modeReason = readonlyMode.value;
            }

            tableData.push([dayDate, plannedHrs, recordedHrs, status, modeReason]);
        }
    }

    if (tableData.length === 0) {
        alert("No table data found to export! Please make sure a week is selected.");
        return;
    }

    // 5. Draw the Table
    doc.autoTable({
        startY: 50,
        head: [['DAY / DATE', 'PLANNED', 'RECORDED', 'STATUS', 'MODE / REASON']],
        body: tableData,
        theme: 'grid',
        headStyles: { fillColor: [30, 58, 138] },
        styles: { fontSize: 10, cellPadding: 4 }
    });

    // 6. Add Totals at the bottom
    const finalY = doc.lastAutoTable.finalY || 50;
    const sumActualElement = document.getElementById("sum-actual");
    const totalActual = sumActualElement ? sumActualElement.innerText : "0.0";

    doc.setFontSize(12);
    doc.setTextColor(0, 0, 0);
    doc.text(`Total Recorded Hours: ${totalActual}`, 14, finalY + 15);

    // 7. Trigger Download
    doc.save(`Timesheet_${empId}_${weekVal}.pdf`);
}