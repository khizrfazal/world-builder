"use client";

import { useState } from "react";
import { createWorld } from "@/actions/worldActions";

export default function WorldForm() {
  const [title, setTitle] = useState("");

  async function onSubmit(e) {
    e.preventDefault();
    await createWorld({ title });
    setTitle("");
  }

  return (
    <form onSubmit={onSubmit} className="flex gap-2">
      <input
        className="border p-2 flex-1"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        placeholder="World name..."
      />
      <button className="bg-black text-white px-4">
        Create
      </button>
    </form>
  );
}