"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { createCharacter } from "@/actions/characterActions";
import { Character } from "@/types/Character";

export default function CharacterSection({
  worldId,
  characters,
}: {
  worldId: string;
  characters: Character[];
}) {
  const router = useRouter();
  const [open, setOpen] = useState(false);
  const [name, setName] = useState("");
  const [summary, setSummary] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleCreate(e: React.FormEvent) {
    e.preventDefault();
    if (!name.trim()) return;

    setLoading(true);
    await createCharacter(worldId, { name: name.trim(), summary: summary.trim() });
    setLoading(false);

    setName("");
    setSummary("");
    setOpen(false);
    router.refresh();
  }

  return (
    <section className="space-y-4">
      <div className="flex items-center justify-between">
        <h2 className="text-xl font-semibold text-stone-900">Characters</h2>
        <button
          onClick={() => setOpen((v) => !v)}
          className="bg-black text-white px-3 py-1.5 rounded-lg text-sm hover:bg-stone-800 transition"
        >
          {open ? "Cancel" : "Add character"}
        </button>
      </div>

      {open && (
        <form
          onSubmit={handleCreate}
          className="p-4 bg-white rounded-xl border border-stone-200 space-y-3"
        >
          <div>
            <label className="block text-sm font-medium text-stone-700 mb-1">
              Name
            </label>
            <input
              value={name}
              onChange={(e) => setName(e.target.value)}
              autoFocus
              className="w-full rounded-lg border border-stone-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-stone-400"
              placeholder="e.g. Lyra Vance"
            />
          </div>
          <div>
            <label className="block text-sm font-medium text-stone-700 mb-1">
              Summary
            </label>
            <textarea
              value={summary}
              onChange={(e) => setSummary(e.target.value)}
              rows={2}
              className="w-full rounded-lg border border-stone-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-stone-400"
              placeholder="A short description of this character"
            />
          </div>
          <div className="flex justify-end">
            <button
              type="submit"
              disabled={loading || !name.trim()}
              className="bg-black text-white px-4 py-2 rounded-lg text-sm hover:bg-stone-800 transition disabled:opacity-50"
            >
              {loading ? "Saving..." : "Save character"}
            </button>
          </div>
        </form>
      )}

      {characters.length === 0 ? (
        <div className="text-center py-12 border border-dashed rounded-xl bg-white">
          <p className="text-sm text-stone-500">
            No characters yet. Add your first one to populate this world.
          </p>
        </div>
      ) : (
        <div className="space-y-3">
          {characters.map((c) => (
            <div
              key={c.id}
              className="p-4 rounded-xl border border-stone-200 bg-white"
            >
              <h3 className="text-base font-semibold text-stone-900">
                {c.name}
              </h3>
              <p className="text-sm text-stone-500 mt-1 line-clamp-2">
                {c.summary || "No summary yet"}
              </p>
            </div>
          ))}
        </div>
      )}
    </section>
  );
}