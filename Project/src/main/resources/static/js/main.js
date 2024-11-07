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
    heroText.classList.remove('animate', 'left', 'right'); // 초기화
    if (index === 0) {
        heroText.classList.add('left', 'animate'); // 첫 번째 슬라이드: 왼쪽 위치
    } else if (index === 1) {
        heroText.classList.add('right', 'animate'); // 두 번째 슬라이드: 오른쪽 위치
    }
}

function nextSlide() {
    currentIndex = (currentIndex + 1) % slides.length;
    showSlide(currentIndex);
}

if (slides.length > 0) {
    slides[0].classList.add('active');
    setInterval(nextSlide, slideInterval);
    showSlide(currentIndex);
}