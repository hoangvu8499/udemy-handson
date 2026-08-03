import React from 'react'

export default function EditEmployee({
  employee,
  ref
}) {
  return (
    <dialog ref={ref} className="result-dialog">
      <h3>Edit Employee</h3>

      <p>
        Name: <strong>{employee?.empName}</strong>
      </p>

      <p>
        Address: <strong>{employee?.empAddress}</strong>
      </p>

      <p>
        Salary: <strong>{employee?.empSalary}</strong>
      </p>

      <form method="dialog">
        <button>close</button>
      </form>
    </dialog>
  );
}