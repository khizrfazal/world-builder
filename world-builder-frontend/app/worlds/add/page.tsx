"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { createWorld } from "@/actions/worldActions";

export default function NewWorldPage() {
  const router = useRouter();
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");

  async function onSubmit(e) {
    e.preventDefault();

    await createWorld({ title, description });

    router.push("/worlds");
  }

  return (
    <div className="max-w-xl mx-auto bg-white p-6 rounded-lg shadow space-y-4">
      <h1 className="text-xl font-bold">Create World</h1>

      <form onSubmit={onSubmit} className="space-y-3">
        <input
          className="w-full border p-2 rounded"
          placeholder="World name"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
        />

        <textarea
          className="w-full border p-2 rounded"
          placeholder="Description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />

        <button className="bg-black text-white px-4 py-2 rounded">
          Create
        </button>
      </form>
    </div>
  );
}