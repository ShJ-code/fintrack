import { useEffect, useState } from "react";
import { createCustomer, deleteCustomer, getCustomers, updateCustomer } from "../api/customers";
import type { Customer } from "../api/types";
import { DataTable, type Column } from "./DataTable";
import { Modal } from "./Modal";

const CustomerPage = () => {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [editing, setEditing] = useState<Customer | "new" | null>(null);
  const [draftName, setDraftName] = useState("");
  const [draftEmail, setDraftEmail] = useState("");
  const [saving, setSaving] = useState(false);

  const reload = async () => {
    setLoading(true);
    try {
      setCustomers(await getCustomers());
      setError(null);
    } catch {
      setError("Failed to load customers");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    let cancelled = false;

    getCustomers()
      .then((nextCustomers) => {
        if (cancelled) return;
        setCustomers(nextCustomers);
        setError(null);
      })
      .catch(() => {
        if (cancelled) return;
        setError("Failed to load customers");
      })
      .finally(() => {
        if (cancelled) return;
        setLoading(false);
      });

    return () => { cancelled = true; };
  }, []);

  const openCreate = () => {
    setEditing("new");
    setDraftName("");
    setDraftEmail("");
  };

  const openEdit = (customer: Customer) => {
    setEditing(customer);
    setDraftName(customer.companyName);
    setDraftEmail(customer.email ?? "");
  };

  const closeModal = () => {
    setEditing(null);
    setDraftName("");
    setDraftEmail("");
  };

  const onSave = async () => {
    if (draftName.trim().length === 0) return;
    setSaving(true);
    try {
      const email = draftEmail.trim() || null;
      if (editing === "new") {
        await createCustomer(draftName.trim(), email);
      } else if (editing) {
        await updateCustomer(editing.customerId, draftName.trim(), email);
      }
      closeModal();
      await reload();
    } catch {
      alert("Save failed");
    } finally {
      setSaving(false);
    }
  };

  const onDelete = async (customer: Customer) => {
    if (!confirm(`Delete "${customer.companyName}"?`)) return;
    try {
      await deleteCustomer(customer.customerId);
      await reload();
    } catch {
      alert("Delete failed");
    }
  };

  const columns: Column<Customer>[] = [
    { header: "ID", render: (customer) => customer.customerId },
    { header: "Company", render: (customer) => customer.companyName },
    { header: "Email", render: (customer) => customer.email || "-" },
  ];

  return (
    <section>
      <header style={{ display: "flex", justifyContent: "space-between", padding: 16 }}>
        <h1 style={{ margin: 0 }}>Customers</h1>
        <button onClick={openCreate}>+ Add customer</button>
      </header>

      {loading && <p style={{ padding: 16 }}>Loading...</p>}
      {error && <p style={{ padding: 16, color: "red" }}>{error}</p>}
      {!loading && !error && (
        <DataTable
          rows={customers}
          columns={columns}
          rowKey={(customer) => customer.customerId}
          onEdit={openEdit}
          onDelete={onDelete}
          emptyMessage="No customers yet - add your first."
        />
      )}

      <Modal
        open={editing !== null}
        title={editing === "new" ? "Add customer" : "Edit customer"}
        onClose={closeModal}
      >
        <div>
          <div>Company name:</div>
          <input value={draftName} onChange={(e) => setDraftName(e.target.value)} autoFocus />
        </div>
        <div style={{ marginTop: 12 }}>
          <div>Email:</div>
          <input type="email" value={draftEmail} onChange={(e) => setDraftEmail(e.target.value)} />
        </div>
        <div style={{ marginTop: 16, display: "flex", gap: 8, justifyContent: "flex-end" }}>
          <button onClick={closeModal}>Cancel</button>
          <button onClick={onSave} disabled={saving || draftName.trim().length === 0}>
            {saving ? "Saving..." : "Save"}
          </button>
        </div>
      </Modal>
    </section>
  );
};

export default CustomerPage;
