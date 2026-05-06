from fastapi import FastAPI
from pydantic import BaseModel
import pandas as pd
from fastapi.staticfiles import StaticFiles

app = FastAPI()

# ----------------------------
# Configuration
# ----------------------------

BASE_URL = "http://192.168.1.4:8000"

# ----------------------------
# Serve Images
# ----------------------------

app.mount(
    "/images",
    StaticFiles(directory="images"),
    name="images"
)

# ----------------------------
# Load Dataset
# ----------------------------

df = pd.read_excel("fashion_products.xlsx")

# Clean column names
df.columns = df.columns.str.strip()

print(df.columns.tolist())

# ----------------------------
# Normalize Image URLs
# ----------------------------

def normalize_image_urls():

    if "image_url" in df.columns:

        df["image_url"] = (
            df["image_url"]
            .astype(str)
            .apply(
                lambda x:
                x if x.startswith("http")
                else f"{BASE_URL}/images/{x}"
            )
        )

normalize_image_urls()

# ----------------------------
# Request Model
# ----------------------------

class SearchRequest(BaseModel):
    query: str


# ----------------------------
# Root
# ----------------------------

@app.get("/")
def home():

    return {
        "message": "API working"
    }


# ----------------------------
# Search Products
# ----------------------------

@app.post("/search")
def search_products(request: SearchRequest):

    query = request.query.lower()

    results = df[
        df["category"]
        .astype(str)
        .str.lower()
        .str.contains(query)
    ]

    return results.to_dict(
        orient="records"
    )


# ----------------------------
# Popular Products
# ----------------------------

@app.get("/popular")
def popular_products():

    results = (
        df.sort_values(
            by="popularity_score",
            ascending=False
        )
    )

    return results.to_dict(
        orient="records"
    )


# ----------------------------
# Pattern Based AI Recommendations
# ----------------------------

@app.get("/recommended/{query}")
def recommended_products(query: str):

    query = query.lower()

    related_patterns = {

        "shirt": [
            "shirt",
            "tshirt",
            "pants",
            "mens wear"
        ],

        "hoodie": [
            "hoodie",
            "cargo pants",
            "joggers"
        ],

        "skirt": [
            "skirt",
            "kurti",
            "womens tshirt",
            "tops"
        ],

        "dress": [
            "dress",
            "heels",
            "handbag"
        ],

        "pants": [
            "pants",
            "shirt",
            "tshirt",
            "hoodie"
        ],

        "kurti": [
            "kurti",
            "skirt",
            "tops"
        ]
    }

    matched_categories = related_patterns.get(
        query,
        [query]
    )

    results = df[
        df["category"]
        .astype(str)
        .str.lower()
        .apply(
            lambda x:
            any(
                item in x
                for item in matched_categories
            )
        )
    ]

    # optional sorting by popularity
    results = results.sort_values(
        by="popularity_score",
        ascending=False
    )

    return results.to_dict(
        orient="records"
    )