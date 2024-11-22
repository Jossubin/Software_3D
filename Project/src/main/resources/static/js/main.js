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