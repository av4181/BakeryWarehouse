$(document).ready(function () {
    // Initialize the live clock
    const clock = $('#clock').FlipClock({
        clockFace: 'TwentyFourHourClock',
        showSeconds: false
    });

    // Function to calculate the time until 22:00 (10 PM) today
    function getTimeUntilTarget() {
        const now = new Date();
        let target = new Date();
        target.setHours(22, 0, 0, 0);

        if (now > target) {
            // If the target time is in the past, set it to 22:00 tomorrow
            target.setDate(target.getDate() + 1);
        }

        const remainingTime = target - now;
        const hours = Math.floor(remainingTime / (1000 * 60 * 60));
        const minutes = Math.floor((remainingTime % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((remainingTime % (1000 * 60)) / 1000);

        return {hours, minutes, seconds};
    }

    // Function to update the countdown timer
    function updateCountdown() {
        const time = getTimeUntilTarget();
        const countdownString = `All products listed underneath will be baked at 10PM! Still ${time.hours}h ${time.minutes}m to go.`;
        $('#countdown').text(countdownString);
    }

    // Initial call to display the countdown immediately
    updateCountdown();

    // Update the countdown timer every second
    setInterval(updateCountdown, 1000);
});
