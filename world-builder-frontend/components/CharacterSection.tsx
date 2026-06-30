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

          <Button onClick={() => setOpen(!open)} size="sm">
            {open ? "Cancel" : "Add"}
          </Button>
        </div>
      </CardHeader>
      <CardContent className="space-y-4">
        {open && (
          <form onSubmit={handleCreate} className="space-y-4">
            <div className="space-y-2">
              <Label>Name</Label>
              <Input value={name} onChange={(e) => setName(e.target.value)} />
            </div>
            <div className="space-y-2">
              <Label>Summary</Label>
              <Textarea value={summary} onChange={(e) => setSummary(e.target.value)} />
            </div>
            <Button disabled={loading}>
              {loading ? "Saving..." : "Create character"}
            </Button>
          </form>
        )}
        <div className="grid gap-2">
          {characters.map((c: any) => (
            <div key={c.id} className="p-3 rounded-lg border">
              <p className="font-medium">{c.name}</p>
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