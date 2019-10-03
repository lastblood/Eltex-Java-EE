package logic;


import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;


public abstract class ACheck extends TimerTask {
    private final Orders orders;
    private Timer t = null;

    //Если periodMillis <= 0, выполнение будет происходить только вызовом метода check()
    //Вызов метода check() никак не влияет на периодичность выполнения Timer
    public ACheck(Orders orders, int periodSeconds) {
        this.orders = orders;

        t = new Timer(true);

        if(periodSeconds > 0) {
            t.scheduleAtFixedRate(this, periodSeconds * 1000, periodSeconds * 1000);
        }
    }

    abstract void check(Orders orders);

    //Для старта в отдельном потоке необходимо запускать этот метод
    public void startCheck() {
        t.schedule(this, 1);
    }

    @Override
    public void run() {
        check(orders);
    }
}
