import { useEffect, useState } from "react";
import { createBill, deleteBill, getBills, updateBill } from "../api/bills";
import { getVendors } from "../api/vendors";
import type { Bill, Vendor } from "../api/types";
import { DataTable, type Column } from "./DataTable";
import { Modal } from "./Modal";

const newDraft = (): Bill => ({
  billId: 0,
  vendorId: 0,
  amount: 0,
  dueDate: "",
  status: "unpaid",
});

const BillPage = () => {
  const [bills, setBills] = useState<Bill[]>([]);
  const [vendors, setVendors] = useState<Vendor[]>([]);
  const [loading, setLoading] = useState(true);
  const [vendorsLoading, setVendorsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [editing, setEditing] = useState<Bill | "new" | null>(null);
  const [draft, setDraft] = useState<Bill>(newDraft);
  const [saving, setSaving] = useState(false);

  const reload = async () => {
    setLoading(true);
    try {
      setBills(await getBills());
      setError(null);
    } catch {
      setError("Failed to load bills");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    let cancelled = false;

    getBills()
      .then((nextBills) => {
        if (cancelled) return;
        setBills(nextBills);
        setError(null);
      })
      .catch(() => {
        if (cancelled) return;
        setError("Failed to load bills");
      })
      .finally(() => {
        if (cancelled) return;
        setLoading(false);
      });

    return () => { cancelled = true; };
  }, []);

  useEffect(() => {
    if (editing === null) return;

    let cancelled = false;

    getVendors()
      .then((nextVendors) => {
        if (cancelled) return;
        setVendors(nextVendors);
        setDraft((current) => (
          current.vendorId === 0 && nextVendors.length > 0
            ? { ...current, vendorId: nextVendors[0].vendorId }
            : current
        ));
      })
      .catch(() => {
        if (cancelled) return;
        alert("Failed to load vendors");
      })
      .finally(() => {
        if (cancelled) return;
        setVendorsLoading(false);
      });

    return () => { cancelled = true; };
  }, [editing]);

  const openCreate = () => {
    setVendorsLoading(true);
    setEditing("new");
    setDraft(newDraft());
  };

  const openEdit = (bill: Bill) => {
    setVendorsLoading(true);
    setEditing(bill);
    setDraft(bill);
  };

  const closeModal = () => {
    setEditing(null);
    setDraft(newDraft());
  };

  const onSave = async () => {
    if (draft.vendorId === 0 || draft.amount <= 0 || draft.dueDate.length === 0) return;
    setSaving(true);
    try {
      if (editing === "new") {
        await createBill(draft);
      } else if (editing) {
        await updateBill(draft);
      }
      closeModal();
      await reload();
    } catch {
      alert("Save failed");
    } finally {
      setSaving(false);
    }
  };

  const onDelete = async (bill: Bill) => {
    if (!confirm(`Delete bill #${bill.billId}?`)) return;
    try {
      await deleteBill(bill.billId);
      await reload();
    } catch {
      alert("Delete failed");
    }
  };

  const columns: Column<Bill>[] = [
    { header: "Vendor", render: (bill) => bill.vendorName ?? bill.vendorId },
    { header: "Amount", render: (bill) => bill.amount },
    { header: "Due date", render: (bill) => bill.dueDate },
    { header: "Status", render: (bill) => bill.status },
  ];

  return (
    <section>
      <header style={{ display: "flex", justifyContent: "space-between", padding: 16 }}>
        <h1 style={{ margin: 0 }}>Bills</h1>
        <button onClick={openCreate}>+ Add bill</button>
      </header>

      {loading && <p style={{ padding: 16 }}>Loading...</p>}
      {error && <p style={{ padding: 16, color: "red" }}>{error}</p>}
      {!loading && !error && (
        <DataTable
          rows={bills}
          columns={columns}
          rowKey={(bill) => bill.billId}
          onEdit={openEdit}
          onDelete={onDelete}
          emptyMessage="No bills yet - add your first."
        />
      )}

      <Modal
        open={editing !== null}
        title={editing === "new" ? "Add bill" : "Edit bill"}
        onClose={closeModal}
      >
        <div>
          <div>Vendor:</div>
          <select
            value={draft.vendorId}
            onChange={(e) => setDraft({ ...draft, vendorId: Number(e.target.value) })}
            disabled={vendorsLoading || vendors.length === 0}
            autoFocus
          >
            {vendors.length === 0 && <option value={0}>No vendors available</option>}
            {vendors.map((vendor) => (
              <option key={vendor.vendorId} value={vendor.vendorId}>
                {vendor.companyName}
              </option>
            ))}
          </select>
        </div>
        <div style={{ marginTop: 12 }}>
          <div>Amount:</div>
          <input
            type="number"
            min="0.01"
            step="0.01"
            value={draft.amount}
            onChange={(e) => setDraft({ ...draft, amount: Number(e.target.value) })}
          />
        </div>
        <div style={{ marginTop: 12 }}>
          <div>Due date:</div>
          <input
            type="date"
            value={draft.dueDate}
            onChange={(e) => setDraft({ ...draft, dueDate: e.target.value })}
          />
        </div>
        <div style={{ marginTop: 12 }}>
          <div>Status:</div>
          <select
            value={draft.status}
            onChange={(e) => setDraft({ ...draft, status: e.target.value })}
          >
            <option value="unpaid">unpaid</option>
            <option value="paid">paid</option>
          </select>
        </div>
        <div style={{ marginTop: 16, display: "flex", gap: 8, justifyContent: "flex-end" }}>
          <button onClick={closeModal}>Cancel</button>
          <button
            onClick={onSave}
            disabled={saving || vendorsLoading || draft.vendorId === 0 || draft.amount <= 0 || draft.dueDate.length === 0}
          >
            {saving ? "Saving..." : "Save"}
          </button>
        </div>
      </Modal>
    </section>
  );
};

export default BillPage;
