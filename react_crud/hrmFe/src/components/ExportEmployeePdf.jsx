import React from 'react';
import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';
import { useTranslation } from "react-i18next";

const ExportEmployeePdf = ({employees}) => {
    const { t } = useTranslation();

  const handleExportPDF = () => {
    // 1. Initialize jsPDF instance (Default: portrait, millimeters, A4)
    const doc = new jsPDF();

    // 2. Add Title / Headers to the Document
    doc.setFont("helvetica", "bold");
    doc.setFontSize(18);
    doc.text("Employee Management Report", 14, 22);

    // 3. Add Subtitle or Timestamp
    doc.setFont("helvetica", "normal");
    doc.setFontSize(10);
    doc.setTextColor(100);
    doc.text(`Generated on: ${new Date().toLocaleDateString()}`, 14, 30);

    // 4. Format columns and rows for jsPDF-autotable
    const tableColumns = ["ID", "Name", "Address", "Salary", "Category"];
    const tableRows = employees.map(employee => [
      employee.empId,
      employee.empName,
      employee.empAddress,
      employee.empSalary,
      employee.catName
    ]);

    // 5. Generate the Table
    autoTable(doc, {
      head: [tableColumns],
      body: tableRows,
      startY: 38, // Start drawing the table below our title text
      styles: { fontSize: 10, cellPadding: 3 },
      headStyles: { fillColor: [41, 128, 185], textColor: 255 }, // Custom theme color
      alternateRowStyles: { fillColor: [245, 245, 245] },
      margin: { top: 20, left: 14, right: 14 }
    });

    // 6. Save the generated PDF
    doc.save("employee-report.pdf");
  };

  return (
    <div style={{ padding: '20px' }}>
      <button 
        onClick={handleExportPDF}
        style={{
        //   padding: '5px 5px',
        //   backgroundColor: '#007bff',
        //   color: '#fff',
        //   border: 'none',
          borderRadius: '4px',
          cursor: 'pointer'
        }}
      >
        {t("exportTable")} 
      </button>
    </div>
  );
};

export default ExportEmployeePdf;
