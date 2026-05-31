import type { ReactNode } from "react";

interface Props {
  open: boolean;
  title: string;
  onClose: () => void;
  children: ReactNode;
}

export const Modal = ({ open, title, onClose, children }: Props) => {
  if (!open) return null;
  return (
    <div
      onClick={onClose}
      style={{
        position: "fixed", inset: 0, background: "rgba(0,0,0,0.4)",
        display: "flex", alignItems: "center", justifyContent: "center", zIndex: 1000,
      }}
    >
      <div
        onClick={(e) => e.stopPropagation()}
        style={{ background: "#222", color: "white", padding: 24, borderRadius: 8, minWidth: 400 }}
      >
        <div style={{ display: "flex", justifyContent: "space-between", marginBottom: 16 }}>
          <h2 style={{ margin: 0 }}>{title}</h2>
          <button onClick={onClose}>×</button>
        </div>
        {children}
      </div>
    </div>
  );
};