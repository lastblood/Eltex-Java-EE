package logic;


import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public abstract class ACheck extends TimerTask {
    private final Orders orders;
    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    //Если periodMillis <= 0, выполнение будет происходить только вызовом метода check()
    //Вызов метода check() никак не влияет на периодичность выполнения Timer
    public ACheck(Orders orders, int periodSeconds) {
        this.orders = orders;

        if(periodSeconds > 0)
            service.scheduleWithFixedDelay(this, periodSeconds, periodSeconds, TimeUnit.SECONDS);
    }

    public ACheck(Orders orders) {
        this.orders = orders;
    }

    abstract void check(Orders orders);

    public void startCheck() {
        service.execute(this);

    }

    @Override
    public void run() {
        check(orders);
    }


}
