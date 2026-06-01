import { useEffect, useState } from "react";
import { createInvoice, deleteInvoice, getInvoices, updateInvoice } from "../api/invoices";
import { getCustomers } from "../api/customers";
import type { Invoice, Customer } from "../api/types";
import { DataTable, type Column } from "./DataTable";
import { Modal } from "./Modal";

const newDraft = (): Invoice => ({
  invoiceId: 0,
  customerId: 0,
  amount: 0,
  dueDate: "",
  status: "unpaid",
});

const InvoicesPage = () => {
  const [invoices, setInvoices] = useState<Invoice[]>([]);
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [loading, setLoading] = useState(true);
  const [customersLoading, setCustomersLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [editing, setEditing] = useState<Invoice | "new" | null>(null);
  const [draft, setDraft] = useState<Invoice>(newDraft);
  const [saving, setSaving] = useState(false);

  const reload = async () => {
    setLoading(true);
    try {
      setInvoices(await getInvoices());
      setError(null);
    } catch {
      setError("Failed to load invoices");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    let cancelled = false;

    getInvoices()
      .then((nextInvoices) => {
        if (cancelled) return;
        setInvoices(nextInvoices);
        setError(null);
      })
      .catch(() => {
        if (cancelled) return;
        setError("Failed to load invoices");
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

    getCustomers()
      .then((nextCustomers) => {
        if (cancelled) return;
        setCustomers(nextCustomers);
        setDraft((current) => (
          current.customerId === 0 && nextCustomers.length > 0
            ? { ...current, customerId: nextCustomers[0].customerId }
            : current
        ));
      })
      .catch(() => {
        if (cancelled) return;
        alert("Failed to load customers");
      })
      .finally(() => {
        if (cancelled) return;
        setCustomersLoading(false);
      });

    return () => { cancelled = true; };
  }, [editing]);

  const openCreate = () => {
    setCustomersLoading(true);
    setEditing("new");
    setDraft(newDraft());
  };

  const openEdit = (invoice: Invoice) => {
    setCustomersLoading(true);
    setEditing(invoice);
    setDraft(invoice);
  };

  const closeModal = () => {
    setEditing(null);
    setDraft(newDraft());
  };

  const onSave = async () => {
    if (draft.customerId === 0 || draft.amount <= 0 || draft.dueDate.length === 0) return;
    setSaving(true);
    try {
      if (editing === "new") {
        await createInvoice(draft);
      } else if (editing) {
        await updateInvoice(draft);
      }
      closeModal();
      await reload();
    } catch {
      alert("Save failed");
    } finally {
      setSaving(false);
    }
  };

  const onDelete = async (invoice: Invoice) => {
    if (!confirm(`Delete invoice #${invoice.invoiceId}?`)) return;
    try {
      await deleteInvoice(invoice.invoiceId);
      await reload();
    } catch {
      alert("Delete failed");
    }
  };

  const columns: Column<Invoice>[] = [
    { header: "Customer", render: (invoice) => invoice.customerName ?? invoice.customerId },
    { header: "Amount", render: (invoice) => invoice.amount },
    { header: "Due date", render: (invoice) => invoice.dueDate },
    { header: "Status", render: (invoice) => invoice.status },
  ];

  return (
    <section>
      <header style={{ display: "flex", justifyContent: "space-between", padding: 16 }}>
        <h1 style={{ margin: 0 }}>Invoices</h1>
        <button onClick={openCreate}>+ Add invoice</button>
      </header>

      {loading && <p style={{ padding: 16 }}>Loading...</p>}
      {error && <p style={{ padding: 16, color: "red" }}>{error}</p>}
      {!loading && !error && (
        <DataTable
          rows={invoices}
          columns={columns}
          rowKey={(invoice) => invoice.invoiceId}
          onEdit={openEdit}
          onDelete={onDelete}
          emptyMessage="No invoices yet - add your first."
        />
      )}

      <Modal
        open={editing !== null}
        title={editing === "new" ? "Add invoice" : "Edit invoice"}
        onClose={closeModal}
      >
        <div>
          <div>Customer:</div>
          <select
            value={draft.customerId}
            onChange={(e) => setDraft({ ...draft, customerId: Number(e.target.value) })}
            disabled={customersLoading || customers.length === 0}
            autoFocus
          >
            {customers.length === 0 && <option value={0}>No customers available</option>}
            {customers.map((customer) => (
              <option key={customer.customerId} value={customer.customerId}>
                {customer.companyName}
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
            disabled={saving || customersLoading || draft.customerId === 0 || draft.amount <= 0 || draft.dueDate.length === 0}
          >
            {saving ? "Saving..." : "Save"}
          </button>
        </div>
      </Modal>
    </section>
  );
};

export default InvoicesPage;
