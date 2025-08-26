import React, { useState } from "react";

interface Card {
  cardHolderName: string;
  cardNumber: string;
  createdDate: string;
}

export default function CardSearch() {
  const [searchPar, setSearchPar] = useState("");
  const [pan, setPan] = useState("");
  const [last4, setLast4] = useState("");
  const [error, setError] = useState("");
  const [results, setResults] = useState<Card[]>([]);

  const handleSearch = async () => {
    
    try{
        const payload = {
            pan: pan,
            last4: last4
        };
        const res = await fetch("http://localhost:8080/v1/cards/search", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
      if (!res.ok) {
        const err = await res.json();
        throw new Error(err.message);
      }
      else{
        const data: Card[] = await res.json();
        setResults(data);
      }
    }
    catch (err: any) {
      setError(err.message);
      console.error("Failed to search card:", err.message || err);
    } 

  };

  function setSearchParam(value: string) {
    setSearchPar(formatPan(value));
    if(value.length === 4){
        setLast4(value);
        setPan("");
    } else {
        setLast4("");
        setPan(value); 
    }        
  } 

    // Format PAN as groups of 4 for display while typing
  function formatPan(value: string): string {
    const d = digitsOnly(value);
    return d.replace(/(.{4})/g, "$1 ").trim();
  }

  // To remove non-digits
  function digitsOnly(value: string): string {
    return value.replace(/\D/g, "");
  }

  return (
    <div className="mt-6">
      <h3 className="text-lg font-semibold mb-2">Search Cards</h3>
      <div className="flex space-x-2">
        <input
          type="text"
          inputMode="numeric"
          pattern="[0-9 ]*"
          maxLength={23} // 19 digits + spaces
          placeholder="Enter full PAN or last 4 digits"
          value={searchPar}
          onChange={(e) => setSearchParam(e.target.value)}
          className="flex-1 px-3 py-2 border rounded-lg"
        />
        <button
          type="button"
          onClick={handleSearch}
          className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700"
        >
          Search
        </button>
      </div>

      {results.length > 0 ? (
        <ul className="mt-3 space-y-2">
          {results.map((card, idx) => (
            <li key={idx} className="p-2 border rounded-lg bg-gray-50">
              <p><strong>Name:</strong> {card.cardHolderName}</p>
              <p><strong>PAN:</strong> {card.cardNumber}</p>
              <p><strong>Created Date:</strong> {card.createdDate}</p>
            </li>
          ))}
        </ul>
      ) : (
        error && <p className="mt-2 text-sm text-red-500">{error}</p>
      )}
    </div>
  );
}



