import "./globals.css";
import { Geist } from "next/font/google";
import { cn } from "@/lib/utils";

const geist = Geist({subsets:['latin'],variable:'--font-sans'});


export default function RootLayout({ children }) {
  return (
    <html lang="en" className={cn("font-sans", geist.variable)}>
      <body className="bg-stone-50 text-stone-900">
        <div className="min-h-screen bg-gradient-to-b from-stone-50 to-stone-100">
          <div className="max-w-5xl mx-auto px-6 py-10">
            {children}
          </div>
        </div>
      </body>
    </html>
  );
}