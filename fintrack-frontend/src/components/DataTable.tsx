import type { ReactNode } from "react";

export interface Column<T> {
  header: string;
  render: (row: T) => ReactNode;
}

interface Props<T> {
  rows: T[];
  columns: Column<T>[];
  rowKey: (row: T) => string | number;
  onEdit?: (row: T) => void;
  onDelete?: (row: T) => void;
  emptyMessage?: string;
}

const cellStyle = { border: "1px solid #ccc", padding: 8 };

export function DataTable<T>({ rows, columns, rowKey, onEdit, onDelete, emptyMessage }: Props<T>) {
  if (rows.length === 0) {
    return <p style={{ padding: 16 }}>{emptyMessage ?? "Nothing here yet."}</p>;
  }
  return (
    <table style={{ width: "100%", borderCollapse: "collapse" }}>
      <thead>
        <tr>
          {columns.map((c) => <th key={c.header} style={cellStyle}>{c.header}</th>)}
          {(onEdit || onDelete) && <th style={cellStyle}>Actions</th>}
        </tr>
      </thead>
      <tbody>
        {rows.map((row) => (
          <tr key={rowKey(row)}>
            {columns.map((c) => <td key={c.header} style={cellStyle}>{c.render(row)}</td>)}
            {(onEdit || onDelete) && (
              <td style={cellStyle}>
                {onEdit && <button onClick={() => onEdit(row)}>Edit</button>}
                {onDelete && (
                  <button onClick={() => onDelete(row)} style={{ marginLeft: 8 }}>
                    Delete
                  </button>
                )}
              </td>
            )}
          </tr>
        ))}
      </tbody>
    </table>
  );
}