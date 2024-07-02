import config from "../js/apikey.js";

// --------------------------------------------------뉴스---------------------------------------------------

document.addEventListener("DOMContentLoaded", () => {
    const NEWS_API_KEY = process.env.NEWS_API_KEY;
    // 여기에 올바른 API 키를 입력하세요.
    const url = `https://newsdata.io/api/1/latest?country=kr&category=environment&apikey=${NEWS_API_KEY}`;
    const newsContainer = document.getElementById("news-container");

    fetch(url)
        .then((response) => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.json();
        })
        .then((data) => {
            console.log(data);
            if (data.results) {
                data.results.forEach((article) => {
                    const newsCard = document.createElement("div");
                    newsCard.className = "news-card";

                    const img = document.createElement("img");
                    img.src =
                        article.image_url || "https://via.placeholder.com/300x200";
                    img.alt = article.title;

                    const body = document.createElement("div");
                    body.className = "news-card-body";

                    const title = document.createElement("div");
                    title.className = "news-card-title";
                    title.innerText = article.title;

                    const description = document.createElement("div");
                    description.className = "news-card-description";
                    description.innerText =
                        article.description || "No description available.";

                    const footer = document.createElement("div");
                    footer.className = "news-card-footer";

                    const link = document.createElement("a");
                    link.href = article.link;
                    link.target = "_blank";
                    link.innerText = "Read more";

                    footer.appendChild(link);
                    body.appendChild(title);
                    body.appendChild(description);
                    newsCard.appendChild(img);
                    newsCard.appendChild(body);
                    newsCard.appendChild(footer);
                    newsContainer.appendChild(newsCard);
                });
            } else {
                console.error("No articles found");
            }
        })
        .catch((error) => {
            console.error("Error fetching news:", error);
        });
});