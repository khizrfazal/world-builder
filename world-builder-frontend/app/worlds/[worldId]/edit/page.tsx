import { wbClient } from "@/utils/client";
import EditWorldForm from "@/components/EditWorldForm";

export default async function EditWorldPage({ params }: any) {
  const { worldId } = await params;

  const world = await wbClient.get(`/worlds/${worldId}`);

  return (
    <div className="space-y-10 max-w-2xl mx-auto">
      <EditWorldForm worldId={worldId} world={world} />
    </div>
  );
}