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

// 피팅 기능을 함수로 분리
function handleFitting() {
    const loader = document.getElementById("loader");
    const productImage = document.querySelector(".product-image img");
    const productTitle = document.querySelector(".product-title").textContent;
    
    // 원본 이미지 경로에서 피팅 이미지 경로 생성
    const originalSrc = productImage.src;
    const fitImageSrc = originalSrc.replace(/(\.[^.]+)$/, '_fit$1');
    
    // 기존 fitting-result가 있다면 제거
    const existingResult = document.querySelector('.fitting-result');
    if (existingResult) {
        existingResult.remove();
    }

    // 로딩 애니메이션 표시
    loader.style.display = "flex";

    // 3초 후에 이미지 확인 및 결과 표시 시작
    setTimeout(() => {
        const checkImage = new Image();
        checkImage.onload = () => {
            loader.style.display = "none";
            const resultSection = document.createElement("div");
            resultSection.classList.add("fitting-result");
            resultSection.innerHTML = `
                <div class="fitting-container">
                    <h1>Fitting Result</h1>
                    <div class="fitting-image-container">
                        <img src="${fitImageSrc}" alt="${productTitle} Fitting Result">
                    </div>
                    <p>Here is the preview of your fitting result.</p>
                    <button onclick="window.location.href='/product-detail/${document.getElementById('productId').value}'" class="back-button">Back to Product</button>
                </div>
            `;
            document.body.appendChild(resultSection);
        };

        checkImage.onerror = () => {
            loader.style.display = "none";
            const resultSection = document.createElement("div");
            resultSection.classList.add("fitting-result");
            resultSection.innerHTML = `
                <div class="fitting-container">
                    <h1>Fitting Result</h1>
                    <div class="fitting-image-container">
                        <img src="${originalSrc}" alt="${productTitle} Fitting Result">
                    </div>
                    <p>피팅 이미지를 찾을 수 없습니다.</p>
                    <button onclick="window.location.href='/product-detail/${document.getElementById('productId').value}'" class="back-button">Back to Product</button>
                </div>
            `;
            document.body.appendChild(resultSection);
        };

        checkImage.src = fitImageSrc;
    }, 3000); // 3초 대기
}

// 아바타 아이콘과 피팅 버튼에 이벤트 리스너 추가
document.getElementById("avata-icon")?.addEventListener("click", handleFitting);
document.getElementById("fitting-button")?.addEventListener("click", handleFitting);

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