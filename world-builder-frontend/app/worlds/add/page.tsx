"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { createWorld } from "@/actions/worldActions";

import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";

export default function NewWorldPage() {
  const router = useRouter();

  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [loading, setLoading] = useState(false);

  async function onSubmit(e: React.FormEvent<HTMLFormElement>) {
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
    <div className="space-y-10 max-w-2xl mx-auto">
      {/* BACKLINK */}
      <Button
        variant="ghost"
        onClick={() => router.push("/worlds")}
        className="px-0"
      >
        ← Back to worlds
      </Button>

      {/* PAGE HEADER */}
      <header className="space-y-2">
        <h1 className="text-3xl font-bold tracking-tight">Create a New World</h1>
        <p className="text-muted-foreground text-sm">
          Start building a universe for your stories, characters, and ideas.
        </p>
      </header>

      {/* FORM CARD */}
      <Card>
        <CardHeader>
          <CardTitle>World Details</CardTitle>
          <CardDescription>
            Give your world a name and a short description.
          </CardDescription>
        </CardHeader>

        <CardContent>
          <form
            id="create-world-form"
            onSubmit={onSubmit}
            className="space-y-6"
          >
            <div className="space-y-2">
              <Label htmlFor="title">World name</Label>
              <Input
                id="title"
                placeholder="The Kingdom of Ash"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                required
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="description">Description</Label>
              <Textarea
                id="description"
                placeholder="Describe your world..."
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                rows={5}
              />
            </div>

            {/* BUTTON BELOW FORM, CENTERED */}
            <div className="flex justify-center pt-4">
              <Button
                type="submit"
                disabled={loading}
                className="bg-black text-white hover:bg-black/90 px-8 py-3 font-semibold"
              >
                {loading ? "Creating..." : "Create World"}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}