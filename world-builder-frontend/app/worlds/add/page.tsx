"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { createWorld } from "@/actions/worldActions";

export default function NewWorldPage() {
  const router = useRouter();
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [loading, setLoading] = useState(false);

  async function onSubmit(e) {
    e.preventDefault();
    setLoading(true);

    try {
      await createWorld({ title, description });
      router.push("/worlds");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="max-w-xl mx-auto">
      <div className="mb-6">
        <h1 className="text-2xl font-bold tracking-tight">
          Create world
        </h1>
        <p className="text-sm text-stone-500 mt-1">
          Build a new universe for your story
        </p>
      </div>
      <form
        onSubmit={onSubmit}
        className="bg-white p-6 rounded-xl border space-y-4"
      >
        <input
          className="w-full border rounded-lg p-3 text-sm focus:outline-none focus:ring-2 focus:ring-black"
          placeholder="World name"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          required
        />
        <textarea
          className="w-full border rounded-lg p-3 text-sm h-28 resize-none focus:outline-none focus:ring-2 focus:ring-black"
          placeholder="Description (optional)"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
        <button
          disabled={loading}
          className="bg-black text-white px-4 py-2 rounded-lg text-sm hover:bg-stone-800 transition disabled:opacity-50"
        >
          {loading ? "Creating..." : "Create World"}
        </button>
      </form>
    </div>
  );
}