import "./globals.css";

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body className="bg-stone-100 text-stone-900">
        <div className="min-h-screen">
          <div className="max-w-5xl mx-auto px-6 py-8">
            {children}
          </div>
        </div>
      </body>
    </html>
  );
}