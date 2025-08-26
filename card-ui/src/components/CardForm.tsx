import React, { useState } from "react";
import CardSearch from "./CardSearch";

interface Card {
  cardHolderName: string;
  cardNumber: string;
  createdDate: string;
}
export default function CardForm() {
  const [createCardformData, setCreateCardformData] = useState<Card>({
    cardHolderName: "",
    cardNumber: "",
    createdDate: "",
  });
  const [cards, setCards] = useState<Card>();
  const [submitted, setSubmitted] = useState<boolean>(false);
  const [error, setError] = useState<string>("");

  // To remove non-digits
  function digitsOnly(value: string): string {
    return value.replace(/\D/g, "");
  }

  // Format PAN as groups of 4 for display while typing
  function formatPan(value: string): string {
    const d = digitsOnly(value);
    return d.replace(/(.{4})/g, "$1 ").trim();
  }

  // Luhn algorithm for basic PAN validation
  function luhnCheck(number: string): boolean {
    const digits = number.split("").reverse().map((d) => parseInt(d, 10));
    let sum = 0;
    for (let i = 0; i < digits.length; i++) {
      let val = digits[i];
      if (i % 2 === 1) {
        val *= 2;
        if (val > 9) val -= 9;
      }
      sum += val;
    }
    return sum % 10 === 0;
  }

  function validate(): boolean {
    const plain = digitsOnly(createCardformData.cardNumber);
    if (!createCardformData.cardHolderName.trim()) {
      setError("Cardholder name is required.");
      return false;
    }
    if (!plain) {
      setError("Card number is required.");
      return false;
    }
    if (plain.length < 13 || plain.length > 19) {
      setError("Card number must be between 13 and 19 digits.");
      return false;
    }
    if (!luhnCheck(plain)) {
      setError("Card number failed Luhn check. Please verify.");
      return false;
    } 

    return true;
  }

  function handlePanChange(e: ChangeEvent<HTMLInputElement>) {
    const raw = e.target.value;
    const formatted = formatPan(raw);
    setCreateCardformData({ ...createCardformData, [e.target.name]: formatted });
  }

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setCreateCardformData({ ...createCardformData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    let isValid = validate();
  if(!isValid){
    setError("Validation failed. Please check the form fields.");
    setSubmitted(false);
    return;
  }
    if(isValid){
        e.preventDefault();
        const payload = {
            cardHolderName: createCardformData.cardHolderName.trim(),
            cardNumber: createCardformData.cardNumber,
            createdDate: new Date().toISOString(),
        };
    try{
        setSubmitted(true);
        const res = await fetch("http://localhost:8080/v1/cards/insert", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
      if (!res.ok) {
        const msg = await res.text();
        setError(msg);
        setSubmitted(false);
        throw new Error(msg || "create Request failed with ${res.status}");
      }
      const data: Card = await res.json();
      setCards(data);

    }
    catch (err: any) {
      console.error("Failed to save card:", err.message || err);
      setError("Failed to save card");
      alert("Failed to save card. Please try again.");
    } finally {
      setSubmitted(false);
    }
    }
  }
  function maskPan(formattedPan: string): string {
    const plain = digitsOnly(formattedPan);
    if (plain.length <= 4) return plain;
    const last4 = plain.slice(-4);
    return "**** **** **** " + last4;
  }

  const handleReset = () => {
    setCreateCardformData({ cardHolderName: "", cardNumber: "", createdDate: "" });
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-4 bg-slate-50">
      <form
        onSubmit={handleSubmit}
        className="w-full max-w-md bg-white p-6 rounded-2xl shadow-md"
        aria-label="Card creation form"
      >
        <h2 className="text-2xl font-semibold mb-4">Create Card</h2>

        <label className="block mb-3">
          <span className="text-sm font-medium">Cardholder Name</span>
          <input
            type="text"
            name="cardHolderName"
            value={createCardformData.cardHolderName}
            onChange={handleChange}
            placeholder="e.g., Sujata Swamy"
            className="mt-1 block w-full rounded-md border-gray-200 shadow-sm focus:ring-2 focus:ring-indigo-200 p-2"
            required
            aria-required="true"
          />
        </label>

        <label className="block mb-3">
          <span className="text-sm font-medium">Card Number (PAN)</span>
          <input
            inputMode="numeric"
            pattern="[0-9 ]*"
            name="cardNumber"
            value={createCardformData.cardNumber}
            onChange={handlePanChange}
            placeholder="1234 5678 9012 3456"
            maxLength={23} // 19 digits + spaces
            className="mt-1 block w-full rounded-md border-gray-200 shadow-sm focus:ring-2 focus:ring-indigo-200 p-2"
            required
            aria-required="true"
          />
        </label>

        {error && <div className="text-sm text-red-600 mb-3">{error}</div>}

        <div className="flex items-center justify-between gap-4">
          <button
            type="submit"
            className="px-4 py-2 bg-indigo-600 text-white rounded-md disabled:opacity-60">
            Save Card
          </button>

          <button
        type="button"
        onClick={handleReset}
        className="flex-1 bg-gray-500 text-white py-2 rounded-lg hover:bg-gray-600"
        >
        Reset
        </button>
        </div>

        {submitted && (
          <div className="mt-5 p-4 bg-green-50 rounded-md border">
            <h3 className="font-medium">Card created</h3>
            <p className="text-sm mt-1">Name: {cards?.cardHolderName}</p>
            <p className="text-sm mt-1">PAN: {maskPan(cards?.cardNumber != undefined ? cards?.cardNumber : "")}</p>
            <p className="text-sm mt-1">Date: {cards?.createdDate}</p>
          </div>
        )}
      </form>

            <CardSearch/>

    </div>
  );
}
