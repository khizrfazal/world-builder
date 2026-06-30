"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { createCharacter } from "@/actions/characterActions";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Label } from "@/components/ui/label";

export default function CharacterSection({ worldId, characters }: any) {
  const router = useRouter();

  const [open, setOpen] = useState(false);
  const [name, setName] = useState("");
  const [summary, setSummary] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleCreate(e: React.FormEvent) {
    e.preventDefault();

    setLoading(true);
    await createCharacter(worldId, { name, summary });

    setName("");
    setSummary("");
    setOpen(false);
    setLoading(false);

    router.refresh();
  }

  return (
    <Card>
      <CardHeader>
        <div className="flex items-center justify-between">
          <CardTitle>Characters</CardTitle>

          {/* FIXED ADD BUTTON */}
          <Button
            onClick={() => setOpen(!open)}
            variant={open ? "outline" : "default"}
          >
            {open ? "Cancel" : "Add Character"}
          </Button>
        </div>
      </CardHeader>

      <CardContent className="space-y-6">
        {/* CREATE FORM */}
        {open && (
          <form
            onSubmit={handleCreate}
            className="space-y-4 border p-4 rounded-lg bg-muted/20"
          >
            <div className="space-y-2">
              <Label>Name</Label>
              <Input
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Character name"
              />
            </div>

            <div className="space-y-2">
              <Label>Summary</Label>
              <Textarea
                value={summary}
                onChange={(e) => setSummary(e.target.value)}
                placeholder="Short description"
              />
            </div>

            {/* FIXED CREATE BUTTON */}
            <Button
              type="submit"
              disabled={loading}
              className="w-full"
            >
              {loading ? "Saving..." : "Create Character"}
            </Button>
          </form>
        )}

        {/* CHARACTER LIST */}
        <div className="grid gap-3">
          {characters.map((c: any) => (
            <div
              key={c.id}
              className="p-4 rounded-lg border bg-muted/30"
            >
              <p className="font-semibold">{c.name}</p>
              <p className="text-sm text-muted-foreground">
                {c.summary || "No description"}
              </p>
            </div>
          ))}
        </div>
      </CardContent>
    </Card>
  );
}
