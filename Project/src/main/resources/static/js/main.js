let currentIndex = 0;
const slides = document.querySelectorAll('.slide');
const slideInterval = 5000; // 슬라이드 전환 간격 (밀리초)
const heroText = document.querySelector('.hero-text');

function showSlide(index) {
    slides.forEach((slide, i) => {
        slide.classList.remove('active');
        if (i === index) {
            slide.classList.add('active');
        }
    });

    // 애니메이션을 위한 클래스 추가
    heroText.classList.remove('animate', 'left', 'right'); // 초기화
    if (index === 0) {
        heroText.classList.add('left', 'animate'); // 첫 번째 슬라이드: 왼쪽 위치
    } else if (index === 1) {
        heroText.classList.add('right', 'animate'); // 두 번째 슬라이드: 오른쪽 위치
    }
    // 추가 슬라이드가 있다면 여기에 조건을 추가하세요
    // 예를 들어, 세 번째 슬라이드가 중앙에 위치해야 한다면:
    // else if (index === 2) {
    //     heroText.classList.add('center', 'animate'); // 별도의 CSS 클래스 필요
    // }
}

function nextSlide() {
    currentIndex = (currentIndex + 1) % slides.length;
    showSlide(currentIndex);
}

if (slides.length > 0) {
    slides[0].classList.add('active');
    showSlide(currentIndex); // 초기 슬라이드 표시
    setInterval(nextSlide, slideInterval); // 슬라이드 자동 전환
}

// Avata 클릭 이벤트와 Lottie 애니메이션 로직
document.getElementById("avata-icon")?.addEventListener("click", function () {
    const loader = document.getElementById("loader");
    const productImage = document.querySelector(".product-image img");
    const productTitle = document.querySelector(".product-title").textContent;

    // 기존 fitting-result가 있다면 제거
    const existingResult = document.querySelector('.fitting-result');
    if (existingResult) {
        existingResult.remove();
    }

    // 로딩 애니메이션 표시
    loader.style.display = "flex";

    setTimeout(() => {
        loader.style.display = "none";

        const resultSection = document.createElement("div"); // section 대신 div 사용
        resultSection.classList.add("fitting-result");
        resultSection.innerHTML = `
            <div class="fitting-container">
                <h1>Fitting Result</h1>
                <div class="fitting-image-container">
                    <img src="${productImage.src}" alt="${productTitle} Fitting Result">
                </div>
                <p>Here is the preview of your fitting result.</p>
                <button onclick="window.location.href='/product-detail/${document.getElementById('productId').value}'" class="back-button">Back to Product</button>
            </div>
        `;

        // body에 직접 추가
        document.body.appendChild(resultSection);
    }, 3000);
});

// 슬라이드 쇼 관련 코드는 홈페이지에서만 필요하다면 조건부로 실행
if (document.querySelector('.slide')) {
    let currentIndex = 0;
    const slides = document.querySelectorAll('.slide');
    const slideInterval = 5000;
    const heroText = document.querySelector('.hero-text');

    function showSlide(index) {
        slides.forEach((slide, i) => {
            slide.classList.remove('active');
            if (i === index) {
                slide.classList.add('active');
            }
        });

        // 애니메이션을 위한 클래스 추가
        heroText.classList.remove('animate', 'left', 'right');
        if (index === 0) {
            heroText.classList.add('left', 'animate');
        } else if (index === 1) {
            heroText.classList.add('right', 'animate');
        }
    }

    function nextSlide() {
        currentIndex = (currentIndex + 1) % slides.length;
        showSlide(currentIndex);
    }

    if (slides.length > 0) {
        slides[0].classList.add('active');
        showSlide(currentIndex);
        setInterval(nextSlide, slideInterval);
    }
}