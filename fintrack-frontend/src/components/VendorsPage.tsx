import { useEffect, useState } from "react";
import { DataTable, type Column } from "./DataTable";
import { Modal } from "./Modal";
import { getVendors, createVendor, updateVendor, deleteVendor } from "../api/vendors";
import type { Vendor } from "../api/types";

const VendorsPage = () => {
  const [vendors, setVendors] = useState<Vendor[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError]     = useState<string | null>(null);

  // Modal state: null means closed; a Vendor means editing; {} sentinel means creating.
  const [editing, setEditing] = useState<Vendor | "new" | null>(null);
  const [draftName, setDraftName] = useState("");
  const [saving, setSaving] = useState(false);

  const reload = async () => {
    setLoading(true);
    try { setVendors(await getVendors()); setError(null); }
    catch (e) { setError("Failed to load vendors"); }
    finally { setLoading(false); }
  };

  useEffect(() => { reload(); }, []);

  const openCreate = () => { setEditing("new"); setDraftName(""); };
  const openEdit   = (v: Vendor) => { setEditing(v); setDraftName(v.companyName); };
  const closeModal = () => { setEditing(null); setDraftName(""); };

  const onSave = async () => {
    if (draftName.trim().length === 0) return;
    setSaving(true);
    try {
      if (editing === "new") await createVendor(draftName.trim());
      else if (editing) await updateVendor(editing.vendorId, draftName.trim());
      closeModal();
      await reload();
    } catch (e) {
      alert("Save failed");
    } finally { setSaving(false); }
  };

  const onDelete = async (v: Vendor) => {
    if (!confirm(`Delete "${v.companyName}"?`)) return;
    try { await deleteVendor(v.vendorId); await reload(); }
    catch { alert("Delete failed"); }
  };

  const columns: Column<Vendor>[] = [
    { header: "ID",      render: (v) => v.vendorId },
    { header: "Company", render: (v) => v.companyName },
  ];

  return (
    <section>
      <header style={{ display: "flex", justifyContent: "space-between", padding: 16 }}>
        <h1 style={{ margin: 0 }}>Vendors</h1>
        <button onClick={openCreate}>+ Add vendor</button>
      </header>

      {loading && <p style={{ padding: 16 }}>Loading…</p>}
      {error   && <p style={{ padding: 16, color: "red" }}>{error}</p>}
      {!loading && !error && (
        <DataTable
          rows={vendors}
          columns={columns}
          rowKey={(v) => v.vendorId}
          onEdit={openEdit}
          onDelete={onDelete}
          emptyMessage="No vendors yet — add your first."
        />
      )}

      <Modal
        open={editing !== null}
        title={editing === "new" ? "Add vendor" : "Edit vendor"}
        onClose={closeModal}
      >
        <div>
          <div>Company name:</div>
          <input value={draftName} onChange={(e) => setDraftName(e.target.value)} autoFocus />
        </div>
        <div style={{ marginTop: 16, display: "flex", gap: 8, justifyContent: "flex-end" }}>
          <button onClick={closeModal}>Cancel</button>
          <button onClick={onSave} disabled={saving || draftName.trim().length === 0}>
            {saving ? "Saving…" : "Save"}
          </button>
        </div>
      </Modal>
    </section>
  );
};

export default VendorsPage;