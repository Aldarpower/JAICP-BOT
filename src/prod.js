function fetchProducts(query) {
    var out = $http.get("https://real-time-product-search.p.rapidapi.com/search?country=ru&q=" + encodeURIComponent(query) + "&language=ru&page=1&limit=3&sort_by=BEST_MATCH&product_condition=ANY&min_rating=ANY", {
        headers: {
            'x-rapidapi-key': 'a7359e1b8emsh73a5a4e297b19b5p1e2a53jsnedcab4f7b3ac', // Замените на ваш ключ
            'x-rapidapi-host': 'real-time-product-search.p.rapidapi.com'
        },
        timeout: 10000
    });
    return out;
}

// Функция для вывода товаров с пагинацией да хуйня просто 3 вывожу может потом доделаю чтоб пользователь мог выбирать кол-во
function showProductPage(page, products) {
    var pageSize = 3;

    // Вычисляем индекс начала и конца текущей страницы
    var startIndex = page * pageSize;
    var endIndex = Math.min(startIndex + pageSize, products.length);

    var message = "";

    // Формируем сообщение с товарами для текущей страницы
    products.slice(startIndex, endIndex).forEach(function (product) {
        message += "- **" + product.product_title + "**\n" +
                   "  Рейтинг: " + product.product_rating + "\n" +
                   "  Цена: " + product.offer.price + "\n" +
                   "  [Купить здесь](" + product.offer.offer_page_url + ")\n\n";
    });

    $reactions.answer(message);
}

function searchprod(query){
    var g = query;
            fetchProducts(g).then(function(response) {
                if (response.status === "OK") {
                    var products = response.data.products;
                    showProductPage(0, products); // Показываем первую страницу
                } else {
                    $reactions.answer("Ошибка при получении данных: " + response.data.status);
                }
            }).catch(function(error) {
                $reactions.answer("Ошибка сети: " + error.message);
            });
}