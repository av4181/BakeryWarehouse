
function toggleRowDetails(rowId) {
    const row = document.getElementById(rowId);
    const details = row.nextElementSibling;
    if (details.style.display === "none") {
        details.style.display = "table-row";
    } else {
        details.style.display = "none";
    }
}